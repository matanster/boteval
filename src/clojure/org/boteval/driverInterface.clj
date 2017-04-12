(ns org.boteval.driverInterface)

(defprotocol Driver
  "a protocol to be implemented by any driver"
  (connectToBot [_ reply-function])
  (openBotSession [_])
  (receiveFromBot [_ session-id message])
  (getReceived [_ session-id])
  (sendToBot [_ session-id message]))

