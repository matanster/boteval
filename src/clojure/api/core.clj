;;; just a main, for testing and playing

(ns api.core (:gen-class)
  (:require [samples.scenarios :as scenarios]))

(defn sayHello []
  (println "Hello, World!"))

(defn -main []
  (sayHello)
  (scenarios/bundle1 "foo")
)
