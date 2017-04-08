;; currently runs the clojure samples, not testing the framework's own code

(ns boteval.core-test
  (:require [clojure.test :refer :all]
            [org.boteval.engine.core :as engine]
            [boteval.test-scenarios :as sample-scenarios]))

(deftest run
  (testing "bundle run"
    (let [context (engine/new-context)]
    (engine/run-scenario sample-scenarios/master-scenario1 "master" context []))))
