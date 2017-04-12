;;; some sample scenarios

(ns boteval.test-scenarios
  (:use [org.boteval.engine.core]
        [org.boteval.engine.api]))

(defn scenario-1 [context session-id]
  (sendToBot session-id "Hi bot")
)

(defn scenario-2 [context session-id]
  (sendToBot session-id "What's up bot?"))

(defn master-scenario-1 [context _]
  (let [session-id (openBotSession)]
    (run-scenario scenario-1 "scenario-1" context session-id)
    (run-scenario scenario-2 "scenario-2" context session-id)
    (println session-id "received " (getReceived session-id))
  )
)


