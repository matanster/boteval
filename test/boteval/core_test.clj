;; currently runs the clojure samples, not testing the framework's own code

(ns boteval.core-test
  (:require [clojure.test :refer :all])
  (:use [org.boteval.engine.core]
        [org.boteval.engine.api]
        [boteval.dumbot.driver]
        [org.boteval.logger.core]
        [boteval.test-scenarios]))

(def head-context
  {:scenario-hierarchy ["head"]})

(deftest run
  (testing "bundle run"
    (init driver default-logger)
    (connectToBot)
    (run-scenario master-scenario-1 "master-scenario-1" head-context [])))
