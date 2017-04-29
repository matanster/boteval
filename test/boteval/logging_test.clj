;; tests for the default logger,
;; implemented as integration tests involving the api

(ns boteval.logging-test
  (:require [clojure.test :refer :all])
  (:use [org.boteval.engine.api]
        [boteval.dumbot.driver]
        [boteval.sample-scenarios]
        [boteval.sampleEvaluators.evaluator]
        [boteval.datastore-clean])
  (:require [clojure.java.io :as io])
  (:require [org.boteval.defaultLogger.core :as logger])
  (:require [honeysql.core :as sql])
  (:require [honeysql.helpers :refer :all]))

(defn ^:private get-logged []
  (let [scenario-executions (logger/get-from-db (-> (select :scenario_id :id :parent_id) (from :scenario_executions)))]
    (println scenario-executions)))

(deftest ^:self concurrnecy-test-1
  #_(datastore-clean)
  (init
      {:name "boteval-self-test"
       :owner "matan"}
       driver logger/default-logger)
  (connectToBot)

  (let [paraphrases (clojure.string/split-lines (slurp (io/file (io/resource "samples/paraphrases.txt"))))
        single-message-runner (fn [message](sendToBot (openBotSession) message))
        run-scenario-for-phrase (fn [text] (run-scenario single-message-runner "phrase" text))]

      (dotimes [n 2]
         (doall (pmap run-scenario-for-phrase paraphrases))))

      (get-logged))



