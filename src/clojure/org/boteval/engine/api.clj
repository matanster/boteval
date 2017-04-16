;; weaving a bot-specific driver into the runtime context

(ns org.boteval.engine.api
  (:require [org.boteval.driverInterface :refer [Driver]]) ; the driver interface
  (:require [org.boteval.loggerInterface :refer [Logger]]) ; the logger interface
  (:require [clj-time.core :as time])
  (:require [clj-time.coerce :as time-convert])
  (:require [clojure.java.shell :as shell])
  (:require [clojure.string :as str])
  (:gen-class))

(defn now [] (time/now))

;; function that initializes the api with a driver and logger
(defn init
  [project-meta driver logger]
  {:pre [(contains? project-meta :name)
         (contains? project-meta :owner)
         (satisfies? Driver driver)
         (satisfies? Logger logger)]}

    (def self-git-hash
      ; http://stackoverflow.com/a/29528037/1509695
      ; http://stackoverflow.com/questions/38934681/how-to-get-timestamp-neutral-git-hash-from-a-given-commit-hash
      ; http://dev.clojure.org/jira/browse/CLJ-124
      (let [{git-hash :out exit-code :exit} (shell/sh "git" "describe" "--always" "--dirty")]
        (if
          (= exit-code 0)
            (str/trimr git-hash)
            (throw (Exception. "failed to get self git hash ― are you running the framework without git installed?")))))

    (println "starting up.. self-hash is" self-git-hash) ; move to plain logger

    (. logger init (assoc project-meta :project-git-hash self-git-hash))

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
        (. logger log-scenario-execution-start scenario-name scenario-hierarchy (time-convert/to-sql-time (now)))
        (println "running scenario" scenario-name "scenario hierarchy being" scenario-hierarchy)
        (fn params)
        (println scenario-hierarchy "finished")
      )
    )

    nil
)

