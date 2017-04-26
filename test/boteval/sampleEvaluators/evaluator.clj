(ns boteval.sampleEvaluators.evaluator
  (:require [clojure.java.io :as io]))

(let [paraphrases (io/file (io/resource "samples/paraphrases.txt"))]
  (-> paraphrases slurp println)
)

