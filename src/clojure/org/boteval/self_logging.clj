(ns org.boteval.self-logging
  #_(:use [taoensso.timbre :only (handle-uncaught-jvm-exceptions! info) :rename {info self-log}])
  (:use [taoensso.timbre])
  (:use [org.boteval.self]))

(let [log-file-name "logs/self/log.log"]
  (set-config! {:level :trace :appenders {:spit (spit-appender {:fname log-file-name :append? true})}}) ;https://github.com/ptaoussanis/timbre/issues/228
  (println "self-logging to" log-file-name))

; set the Sets JVM-global DefaultUncaughtExceptionHandler to log such exceptions before (thread) termination
; https://github.com/ptaoussanis/timbre/blob/0e094753753bc4b78585c1e6e2e803f9afcfd71b/src/taoensso/timbre.cljx#L628
(handle-uncaught-jvm-exceptions!) ; will log the unhandled exception before relegating to the JVM

; logging function for the rest of the project to use
(defn self-log [& message]
  (info (apply str message)))

; log starting up when this namespace is first loaded
(self-log "starting up.. self-hash is " self-git-hash)
