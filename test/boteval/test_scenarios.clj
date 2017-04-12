;;; some sample scenarios

(ns boteval.test-scenarios
  (:use [org.boteval.engine.core]
        [org.boteval.engine.api]))

(defn scenario-1 [session-id]
  (sendToBot session-id "Hi bot")
)

(defn scenario-2 [session-id]
  (sendToBot session-id "What's up bot?"))

(defn master-scenario-1 [_]
  (let [session-id (openBotSession)]
    (run-scenario scenario-1 "scenario-1" session-id)
    (run-scenario scenario-2 "scenario-2" session-id)
    (println session-id "received " (getReceived session-id))
  )
)


