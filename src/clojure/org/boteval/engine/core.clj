(ns org.boteval.engine.core)

(defn sendToBot [message]
  (println "sample driver sending message" message))

(defn new-context []
  (def ^:dynamic context []) ; initialize to empty vec
  context
)

;(def ^:dynamic context []) ; initialize to empty vec

(defn run-scenario [fn scenario-name context params]
  (let [new-context (conj context scenario-name)]
    (println "running scenario" scenario-name ", context is now: " new-context)
    (fn new-context params)
  )
)
