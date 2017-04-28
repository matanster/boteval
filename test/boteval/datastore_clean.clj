;; function for cleaning the logger's datastore, necessary for tests code only
;; todo: move to a namespace unavailable for library users?

(ns boteval.datastore-clean
  (:require [clojure.java.shell])
  (:use [taoensso.timbre :only (info) :rename {info self-log}]))

(defn datastore-clean []
  (self-log "clearing the logger's datastore...")
  (let [result (clojure.java.shell/sh "bash" "-c" "mysql -u root boteval < resources/deploy/mysql.sql")]
    (if (= (:exit result) 0)
      true
      (do
        (println "running the database reinit mysql script returned exit code" (:exit result))
        (println "stdout:" (:out result))
        (println "stderr:" (:err result))
        false))))

