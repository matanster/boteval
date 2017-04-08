;; some sample scenarios

(ns boteval.test-scenarios
  (:use [org.boteval.engine.core]))

(defn scenario-1 [context params]
  (let [driver (:driver context)]
    (.sendToBot driver "Hi bot")))

(defn scenario-2 [context params]
  (let [driver (:driver context)]
    (.sendToBot driver "What's up bot?")))

(defn master-scenario-1 [context params]
  (run-scenario scenario-1 "scenario-1" context params)
  (run-scenario scenario-2 "scenario-2" context params)
)


