;; weaving a bot-specific driver into the runtime context

(ns org.boteval.engine.api
  (:require [org.boteval.driverInterface :refer [Driver]]) ; the driver interface
  (:require [org.boteval.loggerInterface :refer [Logger]]) ; the logger interface
  (:use [org.boteval.time])
  (:use [org.boteval.self])
  (:use [org.boteval.self-logging])
  (:gen-class))

;; function that initializes the api with a driver and logger
(defn init
  [project-meta driver logger]
  {:pre [(contains? project-meta :name)
         (contains? project-meta :owner)
         (satisfies? Driver driver)
         (satisfies? Logger logger)]}

    (. logger init (assoc project-meta :project-git-hash self-git-hash))

    ;; this var is dynamic for the sake of the stack discipline (https://clojure.org/reference/vars) which
    ;; perfectly matches the notion of `run-scenario` keeping track of the scenario hierarchy
    (def ^:dynamic ^:private scenario-execution-hierarchy '())

    (defn receiveFromBotHandler [session-id bot-message]
      (. logger log scenario-execution-hierarchy
         {:text bot-message
          :time (sql-time)
          :is-user false
          :session-id session-id})

      (. driver receiveFromBot session-id bot-message)
      nil)

    (defn connectToBot []
      (. driver connectToBot))

    (defn openBotSession []
      (. driver openBotSession))

    (defn sendToBot [session-id message]
      (. logger log scenario-execution-hierarchy
         {:text message
          :time (sql-time)
          :is-user true
          :session-id session-id})

      (. driver sendToBot session-id message))

    (defn getReceived [session-id]
      (. driver getReceived session-id))

    ;; the function that runs a scenario
    (defn run-scenario [fn scenario-name fn-params]
      (let [scenario-execution-id (. logger log-scenario-execution-start scenario-name scenario-execution-hierarchy (sql-time))]
        (self-log scenario-name " starting")
        (binding [scenario-execution-hierarchy
           (conj scenario-execution-hierarchy {:scenario-name scenario-name :scenario-execution-id scenario-execution-id})]
               (self-log scenario-execution-hierarchy)
               (fn fn-params))
        (self-log scenario-name " finished")
        (. logger log-scenario-execution-end scenario-execution-id (sql-time))))

    nil
)
