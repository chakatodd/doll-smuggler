(ns doll-smuggler.test.read.file
  (:require clojure.string))

(defstruct knapsack-item :name :weight :value :optimal)

(defn parse
  [data]
  (def capacity)
  (def dolls (vector))

  (doseq [line (clojure.string/split-lines data)]

    (if-let [groups (re-matches #"(\w+)\s+(\d+)\s+(\d+)\s+(\d+)" line)]
      (
        (def new-item (struct knapsack-item (nth groups 1) (Integer/parseInt (nth groups 2)) (Integer/parseInt (nth groups 3)) (Integer/parseInt (nth groups 4)))) ;fixme
        (def dolls (conj dolls new-item))
       )
      (if-let [newgroups (re-matches #"max weight:\s+(\d+)" line)]
        (def capacity (second newgroups))
      )
    )
  )
  [dolls, (Integer/parseInt capacity)]
)

(defn get-data
  [filename]
  (parse (slurp filename))
  )

