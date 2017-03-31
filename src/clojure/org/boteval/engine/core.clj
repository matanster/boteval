(ns org.boteval.engine.core)

(defn sendToBot [message]
  (println "sample driver sending message" message))

(defn new-context []
  (def ^:dynamic context []) ; initialize to empty vec
  context
)

(defn run-scenario [fn scenario-name context]
  (binding [context (conj context scenario-name)]
    (println "running scenario" scenario-name ", context is now: " context)
    (fn context)
  )
)
