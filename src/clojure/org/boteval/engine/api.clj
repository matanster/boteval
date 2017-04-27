;; weaving a bot-specific driver into the runtime context

(ns org.boteval.engine.api
  (:require [org.boteval.driverInterface :refer [Driver]]) ; the driver interface
  (:require [org.boteval.loggerInterface :refer [Logger]]) ; the logger interface
  (:require [clj-time.core :as time])
  (:require [clj-time.coerce :as time-convert])
  (:require [clojure.java.shell :as shell])
  (:require [clojure.string :as str])
  (:gen-class))

(defn now []
  "get the current time. this only gives millisecond precision, to get micro or nano precision,
   need to use Java 8's new time object http://stackoverflow.com/a/33472641/1509695 rather than
   clojure's joda-time wrappar"
  (time/now))

(defn sql-time []
  "dbms friendly current time"
  (time-convert/to-sql-time (now)))


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
        (println scenario-name "starting")
        (binding [scenario-execution-hierarchy
           (conj scenario-execution-hierarchy {:scenario-name scenario-name :scenario-execution-id scenario-execution-id})]
               (println scenario-execution-hierarchy)
               (fn fn-params))
        (println scenario-name "finished")
        (. logger log-scenario-execution-end scenario-execution-id (sql-time))))

    nil
)
