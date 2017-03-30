(ns org.boteval.samples.driver
  (:import [org.boteval.api.ApiInterface])
  (:gen-class
   :implements [org.boteval.api.ApiInterface]
   :methods
     [
       [initScenario [String] void]
       [send [String] void]
     ]))

(defn initScenario [name]
  (println "sample driver initializing" name))

(defn sendToBot [message]
  (println "sample driver sending message" message))

