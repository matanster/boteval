(ns org.boteval.engine.core)

;; function for obtaining new top-level context
(defn new-context [head-name]
  (def context {:scenario-hierarchy [head-name]})
  (println context)
  context
)

;; the function that runs a scenario
(defn run-scenario [fn scenario-name context params]
  (let [new-context {:scenario-hierarchy (conj (:scenario-hierarchy context) scenario-name)}]
    (println "running scenario" scenario-name ", context is now: " new-context)
    (fn new-context params)
  )
)
