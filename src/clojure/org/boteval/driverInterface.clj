(ns org.boteval.driverInterface)

(defprotocol Interface
  "a protocol to be implemented by any driver"
  (connectToBot [_ callback-function])
  (openBotSession [_])
  (receiveFromBot [_ session-id])
  (getReceived [_ session-id])
  (sendToBot [_ session-id message]))
