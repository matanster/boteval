(ns org.boteval.loggerInterface)

(defprotocol Logger
  "a protocol to be implemented by any driver"
  (log [_ message]))
