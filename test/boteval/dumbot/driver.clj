;; implementation of a driver, for dumbot. a driver bridges between scenario code
;; and a particular bot implementation, by weaving bot-specific communication within
;; an implementation of the UserApiInterface facade used by scenario authors.

(ns boteval.dumbot.driver
  (:import [org.boteval.api UserApiInterface]) ; api for the scenario writer
  (:require [boteval.dumbot.core :as bot]))    ; dubmot functions

; callback for bot dispatched message handling
(defn receiveFromBot [session-id message]
  (println "message received from bot" message "for session-id" session-id))

; the driver
(def driver
  "a driver for our test bot"
  (reify UserApiInterface
    (connectToBot [this]
      (bot/connect receiveFromBot))
    (openBotSession [this]
      (bot/open-session))
    (sendToBot [this session-id message]
      (println "sample driver sending message" message)
      (bot/receive session-id message)
    )))
