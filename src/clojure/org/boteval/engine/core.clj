;;; the core of boteval's exection engine

(ns org.boteval.engine.core)

;; function for obtaining new top-level context
(defn ^:deprecated new-context [context]
  context
)

(defn ^:deprecated run-scenario-old [fn scenario-name context params]
  (let [new-context (update context :scenario-hierarchy conj scenario-name)]
    (println "running scenario" scenario-name ", scenario-hierarchy is now:" (:scenario-hierarchy new-context))
    (fn new-context params)
    (println "scenario" scenario-name "finished")
  )
)
