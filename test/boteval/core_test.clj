;; currently runs the clojure samples, not testing the framework's own code

(ns boteval.core-test
  (:require [clojure.test :refer :all])
  (:use [org.boteval.engine.api]
        [boteval.dumbot.driver]
        [org.boteval.defaultLogger.core]
        [boteval.test-scenarios]))

(deftest run
  (testing "bundle run"
    (init driver default-logger)
    (connectToBot)
    (run-scenario master-scenario-1 "master-scenario-1" [])))
