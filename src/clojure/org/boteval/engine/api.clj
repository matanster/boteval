;; weaving a bot-specific driver into the runtime context

(ns org.boteval.engine.api
  (:require [org.boteval.driverInterface :refer [Driver]])  ; the driver interface
  (:require [org.boteval.loggerInterface :refer [Logger]])) ; the logger interface

(defn init
  [driver logger]
  {:pre [(satisfies? Driver driver)
         (satisfies? Logger logger)]}

    (defn receiveFromBotHandler [session-id bot-message]
      (. logger log bot-message)
      (. driver receiveFromBot session-id bot-message)
      nil)

    (defn connectToBot []
      (. driver connectToBot receiveFromBotHandler))

    (defn openBotSession []
      (. driver openBotSession))

    (defn sendToBot [session-id message]
      (. driver sendToBot session-id message))

    (defn getReceived [session-id]
      (. driver getReceived session-id))

    nil
)
