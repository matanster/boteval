;;; generator for a function for getting an id for an entity

(ns org.boteval.defaultLogger.core
  (:use clojure.test)
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

(def ^:private index-mirror (atom (sorted-map)))

(defn- get-id-from-db [project-id scenario-name]

  (jdbc/with-db-connection [connection {:datasource datasource}]
    (if-let [project-id
             (:id (first
                    (let [sql-statement (-> (select :id) (from :scenarios) (where [:= :project_id project-id] [:= :name scenario-name]) sql/format)]
                      (jdbc/query connection sql-statement))))]

      project-id

      (do
        ; add to the database and update the index mirror
        (println "adding to scenario to database")
        (let [project-id (first (db-execute (-> (insert-into :scenarios)
                                                (values [{:name scenario-name
                                                          :project_id project-id}]))))]

          (swap! index-mirror (fn [current] (assoc current project-id scenario-name))) ; update the index mirror
          project-id)))))


(defn get-scenario-id [project-id scenario-name]
   "gets a scenario id for the given scenario name, either from a memory cache or from the database"
   (if-let [scenario-id (get index-mirror scenario-name)]
      scenario-id
      (get-id-from-db project-id scenario-name)))





;;; dead code useful for generalizing get-or-create with index mirroring, for more entities
(defn- to-honeysql-where-clause
  "converts a map into a honeysql where clause, as seen in the included :test"
  {:test #(do (is (= (to-honeysql-where-clause {:a 1 :b 2}) (where [:= :a 1] [:= :b 2]))))}
  [entity-map]
  (eval (conj (map (fn [[k v]] (vector := k v)) entity-map) where)))
