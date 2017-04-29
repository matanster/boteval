;; currently runs the clojure samples, not testing the framework's own code

(ns boteval.core-test
  (:require [clojure.test :refer :all])
  (:use [org.boteval.engine.api]
        [boteval.dumbot.driver]
        [org.boteval.defaultLogger.core]
        [boteval.sample-scenarios]
        [boteval.sampleEvaluators.evaluator]
        [boteval.datastore-clean]))

(deftest ^:samples run
    (init
      {:name "boteval-self-test"
       :owner "matan"}
       driver default-logger)
    (connectToBot)
    (run-scenario master-scenario-1 "master-scenario-1" [])
    (run-scenario scenario-3 "scenario-3" []))
