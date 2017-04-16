(ns org.boteval.loggerInterface)

(defprotocol Logger
  "a protocol for a logger"

  (init [_ project-meta])

  (log-scenario-execution-start [_ scenario-name scenario-hierarchy start-time])

  (log [_ scenario-hierarchy message-record])

  (shutdown [_]))
