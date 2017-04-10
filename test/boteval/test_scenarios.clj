;;; some sample scenarios

(ns boteval.test-scenarios
  (:use [org.boteval.engine.core]))

(defn scenario-1 [context session-id]
  (.sendToBot (:driver context) session-id "Hi bot")
)

(defn scenario-2 [context session-id]
  (.sendToBot (:driver context) session-id "What's up bot?"))

(defn master-scenario-1 [context _]
  (let [session-id (.openBotSession (:driver context))]
    (run-scenario scenario-1 "scenario-1" context session-id)
    (run-scenario scenario-2 "scenario-2" context session-id)
    (println session-id "received " (.getReceived (:driver context) session-id))
  )
)


