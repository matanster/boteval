;; currently runs the clojure samples, not testing the framework's own code

(ns boteval.core-test
  (:require [clojure.test :refer :all])
  (:use [org.boteval.engine.core]
        [boteval.test-scenarios]
        [boteval.dumbot.driver]))

(def head-context
  {:scenario-hierarchy ["head"]
   :driver driver})

(deftest run
  (testing "bundle run"
    (.connectToBot driver)
    (run-scenario master-scenario-1 "master-scenario-1" head-context [])))
