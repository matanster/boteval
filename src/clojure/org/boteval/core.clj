;;; just a temporary placeholder main, does nothing

(ns org.boteval.core (:gen-class))

(defn sayHello []
  (println "This does nothing ;-) you probably meant to lein test"))

(defn -main []
  (sayHello)
)
