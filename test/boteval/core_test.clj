;; currently runs the samples, not testing the framework's own code

(ns boteval.core-test
  (:require [clojure.test :refer :all]
            [org.boteval.core :refer :all]
            [org.boteval.samples.scenarios :as sample-scenarios]))

(deftest a-test
  (testing "bundle1 run"
    (sample-scenarios/sample-scenario)))
