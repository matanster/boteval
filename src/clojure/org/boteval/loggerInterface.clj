(ns org.boteval.loggerInterface)

(defprotocol Logger
  "a protocol for a logger"
  (log [_ message-record])
  (shutdown [_]))
