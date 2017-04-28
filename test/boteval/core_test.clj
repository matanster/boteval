;; currently runs the clojure samples, not testing the framework's own code

(ns boteval.core-test
  (:require [clojure.test :refer :all])
  (:use [org.boteval.engine.api]
        [boteval.dumbot.driver]
        [org.boteval.defaultLogger.core]
        [boteval.test-scenarios]
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

(deftest ^:unit-tests concurrnecy-test-1
  (datastore-clean)
  (init
      {:name "boteval-self-test"
       :owner "matan"}
       driver default-logger)
  (connectToBot)
  (require '[clojure.java.io :as io])
  (letfn [(single-message-runner [message]
    (sendToBot (openBotSession) message))]
      (let [paraphrases (clojure.string/split-lines (slurp (clojure.java.io/file (clojure.java.io/resource "samples/paraphrases.txt"))))
        run-scenario-for-phrase (fn [text] (run-scenario single-message-runner "phrase" text))]
          (doall (pmap run-scenario-for-phrase paraphrases)))))

