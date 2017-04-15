(ns org.boteval.loggerInterface)

(defprotocol Logger
  "a protocol for a logger"

  (init [_ project-meta])

  (log [_ scenario-hierarchy message-record])

  #_(log-scenario-execution-start [_ scenario-hierarchy start-time])

  (shutdown [_]))
