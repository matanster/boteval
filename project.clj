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
  :profiles {:test-all
    {:java-source-paths ["src/java-samples"]}}
  :main org.boteval.core)
