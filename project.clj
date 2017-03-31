(defproject boteval "0.1.0-SNAPSHOT"
  :description "the boteval framework"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :source-paths      ["src/clojure"]
  :java-source-paths ["src/java"]
  :javac-options     ["-target" "1.8" "-source" "1.8"]
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :aot [org.boteval.samples.driver]
  :profiles {:java-tests-compile
    {:java-source-paths ["src/java-test"]}}
  :aliases {
    "java-tests" ["do" "compile," "with-profile" "java-tests-compile" "javac," "run" "-m" "org.boteval.javaSamples.JavaUsageTest"]
    "all-tests" ["do" "test," "java-tests"]
  }
  :main org.boteval.core)

; https://github.com/technomancy/leiningen/issues/847#issuecomment-289943710
