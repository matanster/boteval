;; currently runs the clojure samples, not testing the framework's own code

(ns boteval.core-test
  (:require [clojure.test :refer :all])
  (:use [org.boteval.engine.core]
        [boteval.test-scenarios]))

(deftest run
  (testing "bundle run"
    (let [context (new-context "head")]
    (run-scenario master-scenario1 "master" context []))))
