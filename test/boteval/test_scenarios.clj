;; some sample scenarios

(ns boteval.test-scenarios
  (:use [org.boteval.engine.core :as engine])
  (:require [org.boteval.samples.driver :as driver]))

(defn scenario1 [context params]
  (driver/sendToBot "Hi bot"))

(defn scenario2 [context params]
  (driver/sendToBot "Hi bot"))

(defn master-scenario1 [context params]
  (run-scenario scenario1 "scenario1" context params)
  (run-scenario scenario2 "scenario2" context params)
  (println context)
)


