(ns api.core-test
  (:require [clojure.test :refer :all]
            [api.core :refer :all]
            [samples.scenarios :as scenarios]))

(deftest a-test
  (testing "bundle run"
    (scenarios/bundle1 "foo")))
