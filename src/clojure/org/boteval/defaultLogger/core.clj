(ns org.boteval.defaultLogger.core

  " #our default logger

  a component logging messages sent and received to/from the target bots → into the data store,
  the data store being mysql here, we use https://github.com/jkk/honeysql for query building.

  some outstanding TODO items:

  + add time & message quantity buffering for proper performance
  + handle draining the buffer on program exit too "

  (:require [org.boteval.loggerInterface :refer [Logger]])
  (:require [hikari-cp.core :refer :all])
  (:require [clojure.java.jdbc :as jdbc])
  (:require [honeysql.core :as sql])
  (:require [honeysql.helpers :refer :all]))

(load "core_db_util")
(load "core_getScenarioId")

(def default-logger (reify Logger

   ;; init method
   (init
      [this {:keys [name version owner project-git-hash]}]
      {:pre [(some? name)
             (some? owner)
             (some? project-git-hash)]}

        (def project-id
          "the get-or-create-id in/from the database dance, for our project name and owner"
          (if-let [project-id
            (:id (first
              (let [sql-statement (-> (select :id) (from :projects) (where [:= :name name] [:= :owner owner]) sql/format)]
                (jdbc/with-db-connection [connection {:datasource datasource}]
                   (jdbc/query connection sql-statement)))))]

              project-id

              (do
                (println "registering an id for the project")
                (first (db-execute
                  (-> (insert-into :projects)
                      (values [{:name name
                                :owner owner
                                :version_name nil ; later make this an optional argument
                                ;:git_hash project-git-hash
                                }])))))))

        (println "project id is" project-id)

        (def project-git-hash project-git-hash))

    ;; the bot-exchange logging method (messages sent and received from the bot by a bot driver)
    (log
      [this scenario-hierarchy {:keys [text is-user time session-id]}]

      #_(db-execute
         (-> (insert-into :exchanges)
             (values [{:text text
                   :is_user is-user
                   :exchange_time time
                   :session_id session-id
                   :scenario_execution_id 0}]))))

    ;; scenario execution start method
    (log-scenario-execution-start
      [this scenario-name scenario-hierarchy start-time]
      {:pre (= scenario-name (first scenario-hierarchy))}

      (let [scenario-id (get-scenario-id project-id scenario-name)]
        (println scenario-id)
        (let [parent-name (second scenario-hierarchy)]
           (let [insert-payload
              {:scenario_id scenario-id
               :parent_id (if parent-name (get-scenario-id project-id parent-name) nil)
               ;:parent_id (if parent-name (get-scenario-id project-id parent-name) nil)
               :started start-time
               :ended nil}]
                  (db-execute
                    (-> (insert-into :scenario_executions)
                        (values [insert-payload])))))))

    ;; shutdown method
    (shutdown [this]
      (close-datasource datasource))))

