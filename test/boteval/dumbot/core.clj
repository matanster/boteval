(ns boteval.dumbot.core)

(defn- send-to-client [message])

(defn receive [message]
  (println (str "received message " message))
  (send-to-client "got your message, no comments"))




