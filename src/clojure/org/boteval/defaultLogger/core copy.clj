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

;; a hikari connection pool definition
;; (may optimize for performance following https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration)
(def ^:private datasource
  (make-datasource {:driver-class-name "com.mysql.jdbc.jdbc2.optional.MysqlDataSource"
                    :jdbc-url          "jdbc:mysql://localhost/boteval"
                    :username          "boteval"
                    :password          "boteval234%^&"
                    :useSSL            false})) ; using SSL without a trust store for server verification will flood the logs

;; a wrapper for database writing
(defn- db-execute [honey-sql-map]
  (let [sql-statement (sql/format honey-sql-map)]
    (println sql-statement)
       (jdbc/with-db-connection [connection {:datasource datasource}]
          (jdbc/execute! connection sql-statement))))

; a logger
(def default-logger
  (reify Logger

    (init
      [this {:keys [name version owner project-git-hash]}]
      {:pre [(some? name)
             (some? owner)
             (some? project-git-hash)]}

        (def project
          (if-let [project
            (:id (first
              (let [sql-statement (-> (select :id) (from :projects) (where [:= :name name] [:= :owner owner]) sql/format)]
                (jdbc/with-db-connection [connection {:datasource datasource}]
                  (jdbc/query connection sql-statement)))))]

              project

              (do
                (println "registering an id for the project")
                (first (db-execute
                  (-> (insert-into :projects)
                      (values [{:name name
                             :owner owner
                             :version_name nil ; later make this an optional argument
                             ;:git_hash project-git-hash
                             }])))))))

        (println "project id is " project)

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

      #_(log-scenario-execution-start [this scenario-hierarchy start-time]
        (let
          [self-name (first scenario-hierarchy)
           parent-name (nth scenario-hierarchy 1)]

          (db-execute
             (-> (insert-into :scenario_executions)
                 (values [{:scenario_id
                           :parent_id
                           :started start-time
                           :ended nil}])))


    (shutdown [this]
      (close-datasource datasource))
    ))))


