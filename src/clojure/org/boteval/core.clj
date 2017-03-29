;;; just a temporary placeholder main, does nothing

(ns org.boteval.core (:gen-class))
  #_(:require [org.boteval.samples.scenarios :as scenarios])

(defn sayHello []
  (println "This does nothing ;-) you probably meant to lein test"))

(defn -main []
  (sayHello)
)
