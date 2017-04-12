;;
;; a component logging messages sent and received to/from the target bots → into the data store
;;
;; TODO: write to mysql database
;; TODO: add time & message quantity buffering for proper performance
;; TODO: handle draining the buffer on program exit too

(ns org.boteval.logger.core
  (:require [org.boteval.loggerInterface :refer [Logger]]))

; a logger
(def default-logger
  (reify Logger
    (log [this message]
      (println "logged" message))))


