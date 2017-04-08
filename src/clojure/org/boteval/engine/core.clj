(ns org.boteval.engine.core)

;; function for obtaining new top-level context
(defn new-context []
  (def ^:dynamic context []) ; initialize to empty vec
  context
)

;; the function that runs a scenario
(defn run-scenario [fn scenario-name context params]
  (let [new-context (conj context scenario-name)]
    (println "running scenario" scenario-name ", context is now: " new-context)
    (fn new-context params)
  )
)
