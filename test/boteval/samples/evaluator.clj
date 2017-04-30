(ns boteval.samples.evaluator
  (:require [boteval.dumbot.core])
  (:use [boteval.dumbot.driver])
  (:use [org.boteval.engine.api])
  (:require [org.boteval.defaultLogger.core :as logger])
  (:require [honeysql.core :as sql])
  (:require [honeysql.helpers :refer :all])
  (:require [clojure.test :refer :all]))

(defn ^:private call [this & that]
  " calls the function who's symbol name is `this`, with arguments `that`
    credit: http://stackoverflow.com/a/20277152/1509695 "
  (apply (resolve (symbol this)) that))

(defn ^:private test-scenario [_]
  " just a scenario for our test "
  (let [session-id (openBotSession)]
    (sendToBot session-id "hi")))

(defn ^:private get-scenario-executions [scenario-id]
  " gets executions data for given scenario-id "
  {:pre (some? scenario-id)}
    (logger/get-from-db (->
      (select :id :parent_id :started :ended :parameters)
      (from :scenario_executions)
      (where [:= :scenario_id scenario-id]))))


(defn require-scenario [{:keys [scenario-name scenario-project-name]}]
  " function for verifying that a required scenario has already run.
    if it has already run, returns the metadata of all executions of it, otherwise nil"
  {:pre [(some? scenario-name)
         (some? scenario-project-name)]}

  (let
    [scenario-id (logger/get-from-db (->
      (select :s.name :s.project_id :projects.id)
      (from [:scenarios :s] [:projects :projects])
      (where [:= :s.name scenario-name] [:= :projects.name scenario-project-name] [:= :s.project_id :projects.id])))

     ids (count scenario-id)]

    (when (> ids 1) (throw (Exception. (str "only a signle scenario id was expected but " ids " returned from query"))))
    (if (= ids 1)
       (get-scenario-executions scenario-id)
       nil)))

(defn require-scenario-or-run [{:keys [scenario-name scenario-project-name] :as input-map} scenario-fn]
  {:pre [(function? scenario-fn)]}
  " same as require-scenario, only runs the given scenario function, if no existing scenario execution matches.
    note that this is obviously only useful, when the scenario function belongs in the same project "
  (if-not (require-scenario input-map)
    (run-scenario scenario-fn [])))

(defn sample-evaluator []
  (require-scenario-or-run
    {:scenario-name "boteval.samples.evaluator/scenario" :scenario-project-name "boteval-self-test"}
    test-scenario))


(deftest ^:self evaluators
  (init
    {:name "boteval-self-test"
     :owner "matan"}
     driver
     logger/default-logger)
  (connectToBot)

  (sample-evaluator))
