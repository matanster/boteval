;;
;; a component logging messages sent and received to/from the target bots → into the data store
;; we use https://github.com/jkk/honeysql for query building here.
;;
;; TODO: add time & message quantity buffering for proper performance
;; TODO: handle draining the buffer on program exit too

(ns org.boteval.defaultLogger.core
  (:require [org.boteval.loggerInterface :refer [Logger]])
  (:require [hikari-cp.core :refer :all])
  (:require [clojure.java.jdbc :as jdbc])
  (:require [honeysql.core :as sql])
  (:require [honeysql.helpers :refer :all]))

(load "helpers")

; a logger
(def default-logger
  (reify Logger

    (init
      [this {:keys [name version owner project-git-hash]}]
      {:pre [(some? name)
             (some? owner)
             (some? project-git-hash)]}

        (def project-id
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

        #_(def project-scenarios
          (let [sql-statement (-> (select :id) (from :scenarios) (where [:= :project_id project-id]) sql/format)]
            (jdbc/with-db-connection [connection {:datasource datasource}]
               (jdbc/query connection sql-statement))))

        #_(println project-scenarios)

        (println "project id is " project-id)

        (def project-git-hash project-git-hash))

    (log
      [this scenario-hierarchy {:keys [text is-user time session-id]}]

      #_(db-execute
         (-> (insert-into :exchanges)
             (values [{:text text
                   :is_user is-user
                   :exchange_time time
                   :session_id session-id
                   :scenario_execution_id 0}])))

      (println "logged" text))


    (log-scenario-execution-start [this scenario-name scenario-hierarchy start-time]
       (let [scenario-id (get-scenario-id project-id scenario-name)]
         (println scenario-id))

       #_(let
          [self-name (first scenario-hierarchy)
           parent-name (nth scenario-hierarchy 1)]

          (db-execute
             (-> (insert-into :scenario_executions)
                 (values [{:scenario_id
                           :parent_id
                           :started start-time
                           :ended nil}])))))


    (shutdown [this]
      (close-datasource datasource))
    ))


