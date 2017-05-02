(ns org.boteval.engine.evaluator-api

  " api to be used by user code "

  (:require [org.boteval.driverInterface :refer [Driver]]) ; the driver interface
  (:require [org.boteval.loggerInterface :refer [Logger]]) ; the logger interface
  (:use [org.boteval.time])
  (:use [org.boteval.self])
  (:use [org.boteval.self-logging])
  (:require [cheshire.core :as json])
  (:require [honeysql.core :as sql])
  (:require [honeysql.helpers :refer :all])
  (:gen-class))


(defn scenario-specifier [arg logger]
  " typing function "
  (cond
    (and (map? arg) (every? arg [:scenario-name :scenario-project-name])) :scenario-unique-key
    (number? arg) :scenario-id
    :default (throw (Exception. "invalid scenario specification"))))

(defmulti get-scenario-executions
  " returns all executions for given scenario "
  scenario-specifier)

(defmethod get-scenario-executions :scenario-id
  [scenario-id logger]
  " returns the executions for given database-assgined scenario-id "
  {:pre (some? scenario-id)}
    (. logger get-from-db (->
      (from :scenario_executions)
      (select :scenario-id :id :parent_id :started :ended :parameters)
      (where [:= :scenario_id scenario-id]))))

(defmethod get-scenario-executions :scenario-unique-key
  [{:keys [scenario-name scenario-project-name]} logger]
   " returns the executions for given scenario identification "
    {:pre [(some? scenario-name)
           (some? scenario-project-name)]}

  (let
    [scenario-id-rows
       (. logger get-from-db (->
         (select :s.id)
         (from [:scenarios :s] [:projects :projects])
         (where [:= :s.name scenario-name] [:= :projects.name scenario-project-name] [:= :s.project_id :projects.id])))

     ids (count scenario-id-rows)]

     (println scenario-id-rows)

        (cond
          (> ids 1) (throw (Exception. (str "error in uniquely identifying the input scenario: only one scenario id was expected but " ids " ids were found in the database. oops ...")))
          (= ids 1) (get-scenario-executions (:id (first scenario-id-rows)) logger)
          :default nil)))


(defn get-latest-scenario-execution [scenario-specifier logger]
  " returns the latest execution for given scenario "
  " NOTE: latest is elusive semantics. one execution might start after the other but finish first; which one is latest then? "
  (let
    [executions (get-scenario-executions scenario-specifier logger)]
    (reverse (sort-by :started executions)))) ; latest here means latest started


