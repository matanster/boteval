;; a sample driver written in clojure, useful also for testing the framework

(ns org.boteval.samples.driver
  (:import [org.boteval.api.ApiInterface])
  (:gen-class
   :implements [org.boteval.api.ApiInterface]
   :methods
     [
       [initScenario [String] void]
       [send [String] void]
     ]))

(defn initScenario [scenario-name]
  (def ^:dynamic scenario-tree scenario-name)
  (println "sample driver initializing" scenario-name ", context is now: " scenario-tree)
)

(defn sendToBot [message]
  (println "sample driver sending message" message))

