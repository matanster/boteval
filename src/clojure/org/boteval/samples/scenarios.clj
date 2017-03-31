(ns org.boteval.samples.scenarios
  (:require [org.boteval.engine.core :as engine])
  (:require [org.boteval.samples.driver :as driver]))


(defn scenario1 [context]
  (driver/sendToBot "Hi bot"))

(defn scenario2 [context]
  (driver/sendToBot "Hi bot"))

(defn master-scenario1 [context]
  (engine/run-scenario scenario1 "scenario1" context)
  (engine/run-scenario scenario2 "scenario2" context))


