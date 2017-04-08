;; implementation of a driver, for dumbot

(ns boteval.dumbot.driver
  (:import [org.boteval.api ApiInterface]))

(def driver
  "a driver for our test bot"
  (reify ApiInterface
    (sendToBot [this message]
      (println "sample driver sending message" message))))
