;; a sample driver written in clojure, useful also for testing the framework
;; this here uses the gen-class approach to implementing an interface

(ns org.boteval.samples.driver
  (:import [org.boteval.api.ApiInterface])
  (:gen-class
   :implements [org.boteval.api.ApiInterface]
   :methods
     [
       [send [String] void]
     ]))

(defn sendToBot [message]
  (println "sample driver sending message" message))

