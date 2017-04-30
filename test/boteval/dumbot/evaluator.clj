(ns boteval.dumbot.evaluator
  (:require [boteval.dumbot.core])
  (:require [boteval.dumbot.driver])
  (:use [org.boteval.engine.api]))

(defn scenario []
  (let [session-id (openBotSession)]
    (sendToBot session-id "hi")))

(defn require-scenario [{:keys [scenario-name scenario-project-name]} force?]
  {:pre [(some? scenario-name)
         (some? scenario-project-name)]}

  "TBD asserts that the required scenarios have already run"
  ; check if had already run
  nil)

(defn dumbot-evaluator []
  (require-scenario ["scenario" "foo"]))
