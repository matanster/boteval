(ns org.boteval.loggerInterface)

(defprotocol Logger
  "a protocol for a logger"
  (log [_ self-hash scenario-hierarchy message-record])
  #_(log-scenario-start [_ scenario-hierarchy start-time])
  (shutdown [_]))
