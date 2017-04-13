;; weaving a bot-specific driver into the runtime context

(ns org.boteval.engine.api
  (:require [org.boteval.driverInterface :refer [Driver]])  ; the driver interface
  (:require [org.boteval.loggerInterface :refer [Logger]])
  (:gen-class)) ; the logger interface

;; function that initializes the api with a driver and logger
(defn init
  [driver logger]
  {:pre [(satisfies? Driver driver)
         (satisfies? Logger logger)]}

    (defn receiveFromBotHandler [session-id bot-message]
      (. logger log bot-message)
      (. driver receiveFromBot session-id bot-message)
      nil)

    (defn connectToBot []
      (. driver connectToBot))

    (defn openBotSession []
      (. driver openBotSession))

    (defn sendToBot [session-id message]
      (. driver sendToBot session-id message))

    (defn getReceived [session-id]
      (. driver getReceived session-id))

    (def ^:dynamic ^:private scenario-hierarchy ["head"]) ;; this var is dynamic for the sake of the stack discipline (https://clojure.org/reference/vars)

    nil
)

;; the function that runs a scenario
(defn run-scenario [fn scenario-name params]
  (println scenario-hierarchy)
  (binding [scenario-hierarchy (conj scenario-hierarchy scenario-name)]
    (println "running scenario" scenario-name "scenario hierarchy being" scenario-hierarchy)
    (fn params)
    (println scenario-hierarchy "finished")
  )
)
