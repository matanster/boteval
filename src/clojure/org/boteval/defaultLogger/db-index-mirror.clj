;;
;; code useful for generalizing get-or-create with index mirroring, as much as the honeysql part goes,
;; for more entities than just the scenarios index. this avoids every scenario run doing a fetch-or-insert
;; against the database, while being monotonously resilient to concurrent runners on other servers being active
;; or other future concurrency schemes
;;
;; in such an emodiment, we would likely be using a function generator to return a `get` function
;; per table holding an index we wish to mirror (the generated function feing fed an atom as a closure,
;; by its the function generating it, to preserve namespace hygene.
;;

(defn- ^:deprecated to-honeysql-where-clause
  "converts a map into a honeysql where clause, as seen in the included :test"
  {:test #(do (is (= (to-honeysql-where-clause {:a 1 :b 2}) (where [:= :a 1] [:= :b 2]))))}
  [entity-map]
  (eval (conj (map (fn [[k v]] (vector := k v)) entity-map) where)))
