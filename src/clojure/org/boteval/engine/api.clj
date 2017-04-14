;; weaving a bot-specific driver into the runtime context

(ns org.boteval.engine.api
  (:require [org.boteval.driverInterface :refer [Driver]])  ; the driver interface
  (:require [org.boteval.loggerInterface :refer [Logger]])
  (:require [clj-time.core :as time])
  (:require [clj-time.coerce :as time-convert])
  (:gen-class)) ; the logger interface

(defn now [] (time/now))

;; function that initializes the api with a driver and logger
(defn init
  [driver logger]
  {:pre [(satisfies? Driver driver)
         (satisfies? Logger logger)]}

    ;; this var is dynamic for the sake of the stack discipline (https://clojure.org/reference/vars) which
    ;; perfectly matches the notion of `run-scenario` keeping track of the scenario hierarchy
    (def ^:dynamic ^:private scenario-hierarchy '("root"))

    (defn receiveFromBotHandler [session-id bot-message]
      (let [message-record
        {:text bot-message
         :time (time-convert/to-sql-time (now))
         :is-user false
         :session-id session-id}]
            (. logger log scenario-hierarchy message-record))

      (. driver receiveFromBot session-id bot-message)
      nil)

    (defn connectToBot []
      (. driver connectToBot))

    (defn openBotSession []
      (. driver openBotSession))

    (defn sendToBot [session-id message]
      (let [message-record
        {:text message
         :time (time-convert/to-sql-time (now))
         :is-user true
         :session-id session-id}]
            (. logger log scenario-hierarchy message-record))

      (. driver sendToBot session-id message))

    (defn getReceived [session-id]
      (. driver getReceived session-id))

    ;; the function that runs a scenario
    (defn run-scenario [fn scenario-name params]
      (println scenario-hierarchy)
      (binding [scenario-hierarchy (conj scenario-hierarchy scenario-name)]
        #_(. logger log-scenario-start scenario-hierarchy (time-convert/to-sql-time (now)))
        (println "running scenario" scenario-name "scenario hierarchy being" scenario-hierarchy)
        (fn params)
        (println scenario-hierarchy "finished")
      )
    )


    nil
)

