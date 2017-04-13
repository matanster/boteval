(defproject boteval "0.1.0-SNAPSHOT"
  :description "the boteval framework"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :source-paths      ["src/clojure"]
  :java-source-paths ["src/java"]
  :javac-options     ["-target" "1.8" "-source" "1.8"]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [io.aviso/pretty "0.1.33"] ; pretty exceptions in leinigen
                 [honeysql "0.8.2"]         ; sort of a query
                 [mysql/mysql-connector-java "5.1.41"] ; mysql jdbc driver
                 [hikari-cp "1.7.5"]]       ; jdbc connection pooling, if we'll need it
  :plugins [[io.aviso/pretty "0.1.33"]]
  :aot [org.boteval.engine.api]
  :profiles {:java-tests-compile
    {:java-source-paths ["src/java-test"]}}
  :aliases {
    "java-tests" ["do" "compile," "with-profile" "java-tests-compile" "javac," "run" "-m" "org.boteval.java.samples.ScenarioRunner"]
    "all-tests" ["do" "test," "java-tests"]
  }
  :main org.boteval.core)

; https://github.com/technomancy/leiningen/issues/847#issuecomment-289943710
