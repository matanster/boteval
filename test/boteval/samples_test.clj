(ns boteval.samples-test

  " runs the clojure samples, as that it only tests that they do not throw "

  (:require [clojure.test :refer :all])
  (:use [org.boteval.engine.api]
        [boteval.dumbot.driver]
        [org.boteval.defaultLogger.core]
        [boteval.samples.scenarios]
        [boteval.datastore-clean]))

(deftest ^:samples run
    (init
      {:name "boteval-self-test"
       :owner "matan"}
       driver default-logger)
    (connectToBot)
    (run-scenario master-scenario-1 "master-scenario-1" [])
    (run-scenario scenario-3 "scenario-3" []))
