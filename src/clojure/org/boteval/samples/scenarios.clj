(ns org.boteval.samples.scenarios
  (:require [org.boteval.samples.driver :as driver]))

(defn sample-scenario []
  (driver/initScenario "sample-scenario")
  (driver/sendToBot "Hi bot"))


