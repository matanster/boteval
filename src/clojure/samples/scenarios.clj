(ns samples.scenarios)

(defn scenario1 [param]
  (println "scenario1")
)

(defn scenario2 [param]
  (println "scenario2")
)

(defn bundle1 [param]
  (scenario1 param)
  (scenario2 param)
)

