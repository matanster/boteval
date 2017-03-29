;;
;; a component logging messages sent and received to/from the target bots → into the data store
;;
;; TODO: write to mysql database
;; TODO: add time & message quantity buffering for proper performance
;; TODO: handle draining the buffer on program exit too

(ns boteval.logger)

(defn log [message]
  (println "logged" message))


