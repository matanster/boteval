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
(def datasource
  (make-datasource {:driver-class-name "com.mysql.jdbc.jdbc2.optional.MysqlDataSource"
                    :jdbc-url          "jdbc:mysql://localhost/boteval_new"
                    :username          "boteval"
                    :password          "boteval234%^&"
                    :useSSL            false})) ; using SSL without a trust store for server verification will flood the logs

; a logger
(def default-logger
  (reify Logger
    (log [this scenario-hierarchy {:keys [text is-user time session-id]}]

      (let [
        insert
          (-> (insert-into :sessions)
              (values [{:text text
                        :is_user is-user
                        :time time
                        :session_id session-id}])
              sql/format)]

        (println insert)
        (jdbc/with-db-connection [conn {:datasource datasource}]
          (jdbc/execute! conn insert)))

      (println "logged" text))

    #_(log-scenario-start [this scenario-hierarchy start-time]
      (let
        [self-name (first scenario-hierarchy)
         parent-name (nth scenario-hierarchy 1)]
           (let [insert
              (-> (insert-into :scenario_executions)
              (values [{:name self-name
                        :parent parent-name
                        :started start-time}])
              sql/format)])))

    (shutdown [this]
      (close-datasource datasource))
    ))


