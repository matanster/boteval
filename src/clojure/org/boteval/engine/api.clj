;; implementation of a driver, for dumbot. a driver bridges between scenario code
;; and a particular bot implementation, by weaving bot-specific communication within
;; an implementation of the UserApiInterface facade used by scenario authors.

(ns org.boteval.engine.api)

(defn init [driver logger]

    (defn receiveFromBotHandler [session-id bot-message]
      (. logger log bot-message)
      nil)

    (defn connectToBot []
      (. driver connectToBot receiveFromBotHandler))

    (defn openBotSession []
      (. openBotSession driver))

    (defn sendToBot [session-id message]
      (. sendToBot driver session-id message))

    (defn getReceived [session-id]
      (. getReceived driver session-id))

    nil
)
