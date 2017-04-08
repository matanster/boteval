(ns org.boteval.engine.core)

;; function for obtaining new top-level context
(defn new-context [context]
  context
)

;; the function that runs a scenario
(defn run-scenario [fn scenario-name context params]
  (let [new-context (update context :scenario-hierarchy conj scenario-name)]
    (println "running scenario" scenario-name ", scenario-hierarchy is now:" (:scenario-hierarchy new-context))
    (fn new-context params)
    (println "scenario" scenario-name "finished")
  )
)
