(ns org.boteval.defaultLogger.core
  " helper extension for org.boteval.defaultLogger.core ―
    exposes a function that gets-or-sets an id for a given scenario without much database access "

  (:use clojure.test)
  (:require [hikari-cp.core :refer :all])
  (:require [clojure.java.jdbc :as jdbc])
  (:require [honeysql.core :as sql])
  (:require [honeysql.helpers :refer :all]))

(def ^:private index-mirror (atom (sorted-map)))

(defn ^:private get-id-from-db [project-id scenario-name]

  ;; helper function for querying for an id
  (letfn [(from-db [connection]
            (:id (first
               (let [sql-statement (-> (select :id) (from :scenarios) (where [:= :project_id project-id] [:= :name scenario-name]) sql/format)]
                  (jdbc/query connection sql-statement)))))]

  (jdbc/with-db-connection [connection {:datasource datasource}]
    (if-let [scenario-id (from-db connection)]
      scenario-id

      (do
        ; add to the database, get the auto-incremented key, and update the index mirror with it
        ; we avoid dbms specific ways of getting the auto-increment, to remain dbms neutral.
        (println "adding scenario " scenario-name "to database")
        (let [scenario-id (do (db-execute (-> (insert-into :scenarios)
                                              (values [{:name scenario-name
                                                        :project_id project-id}])))
                              (from-db connection))]


          (println scenario-id)
          (swap! index-mirror (fn [current] (assoc current scenario-name scenario-id))) ; update the index mirror
          scenario-id))))))


(defn get-scenario-id [project-id scenario-name]
   "gets a scenario id for the given scenario name, either from a memory cache or from the database"
   (if-let [scenario-id (get index-mirror scenario-name)]
      scenario-id
      (get-id-from-db project-id scenario-name)))

