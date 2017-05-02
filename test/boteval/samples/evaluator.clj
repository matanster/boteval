(ns boteval.samples.evaluator
  (:require [boteval.dumbot.core])
  (:use     [boteval.dumbot.driver])
  (:use     [org.boteval.engine.api])
  (:use     [org.boteval.engine.evaluator-api])
  (:use     [org.boteval.self])
  (:require [org.boteval.util :as util])
  (:require [org.boteval.defaultLogger.core :as logger])
  (:require [honeysql.core :as sql])
  (:require [honeysql.helpers :refer :all])
  (:require [clojure.test :refer :all]))

(defn ^:private test-scenario [_]
  " just a scenario for our test "
  (let [session-id (openBotSession)]
    (sendToBot session-id "hi")))

#_(defn require-scenario-or-run [{:keys [scenario-name scenario-project-name] :as input-map} scenario-fn]
  {:pre [(function? scenario-fn)]}
  " same as require-scenario, only runs the given scenario function, if no existing scenario execution matches.
    note that this is obviously only useful, when the scenario function belongs in the same project as this code"
  (if-not (require-scenario input-map)
    (run-scenario scenario-fn [])))


(defn sample-evaluator [logger]
  " sample evaluator, that inspects data of the last execution of a scenario, or runs it for the first time "
  (let [latest
    (get-latest-scenario-execution
      {:scenario-name (util/clean-fn-name test-scenario) :scenario-project-name "boteval-self-test"}
      logger)]
    #_test-scenario

    (println latest)))


(deftest ^:self evaluators
  (let [logger logger/default-logger]
    (init
       self
       driver
       logger)
    (connectToBot)
    #_(println (util/clean-fn-name test-scenario))
    (sample-evaluator logger)))
