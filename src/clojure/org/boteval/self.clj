; self identification details, for use in logging and self-logging alike

(ns org.boteval.self
  (:require [clojure.string :as str])
  (:require [clojure.java.shell :as shell]))

(def self-git-hash
  ; http://stackoverflow.com/a/29528037/1509695
  ; http://stackoverflow.com/questions/38934681/how-to-get-timestamp-neutral-git-hash-from-a-given-commit-hash
  ; http://dev.clojure.org/jira/browse/CLJ-124
  (let [{git-hash :out exit-code :exit} (shell/sh "git" "describe" "--always" "--dirty")]
    (if
      (= exit-code 0)
      (str/trimr git-hash)
      (throw (Exception. "failed to get self git hash ― are you running the framework without git installed?")))))
