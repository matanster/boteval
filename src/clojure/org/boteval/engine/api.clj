(ns org.boteval.engine.api

  " the api to be used by user code "

  (:require [org.boteval.driverInterface :refer [Driver]]) ; the driver interface
  (:require [org.boteval.loggerInterface :refer [Logger]]) ; the logger interface
  (:use [org.boteval.time])
  (:use [org.boteval.self])
  (:use [org.boteval.self-logging])
  #_(:require [clojure.repl]) ; for demunge if we'll need it after all
  (:gen-class))


(defn init
  " initializes the api functions to use the given driver and logger
    todo: this is not concurrency-safe, one init will overwrite the other.
    todo: consider a design providing the api not through `defn` per api endpoint (?) "
  [project-meta driver logger]
  {:pre [(contains? project-meta :name)
         (contains? project-meta :owner)
         (satisfies? Driver driver)
         (satisfies? Logger logger)]}

    (self-log "api initializing with given driver and logger")

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

    (defn ^:private run-scenario-impl [fn scenario-name fn-params]
      " this is the function that runs a scenario
        a scenario should always be run through this function, other than during its development "
      (let [scenario-execution-id (. logger log-scenario-execution-start scenario-name scenario-execution-hierarchy (sql-time))]
        (binding [scenario-execution-hierarchy
           (conj scenario-execution-hierarchy {:scenario-name scenario-name :scenario-execution-id scenario-execution-id})]
               #_(self-log scenario-execution-hierarchy)
               (fn fn-params))
        (. logger log-scenario-execution-end scenario-execution-id (sql-time))))

    nil
)

(defn clean-fn-name [fn-name]
  (clojure.string/replace (clojure.string/replace fn-name #"@(.*)" "") "$" "/"))

(defmacro run-scenario [fn-name fn-params]
   (list 'run-scenario-impl fn-name (list 'clean-fn-name (list 'str fn-name)) fn-params))
