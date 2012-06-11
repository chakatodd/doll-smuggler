(ns doll-smuggler.core
  (:use [doll-smuggler.read.file]))

(declare mm) ;forward decl for memoization function
 
(def empty_set [0 []])

(defn- m [items item_index max_weight]
  (cond
    (< item_index 0) empty_set
    (= max_weight 0) empty_set
    true
    (let [{item_weight :weight item_value :value} (get items item_index)]
      (if (> item_weight max_weight)
        (mm items (dec item_index) max_weight)
        (let [[value_n set_n :as no]  (mm items (dec item_index) max_weight)
              [value_y set_y :as yes] (mm items (dec item_index) (- max_weight item_weight))]
          (if (> (+ value_y item_value) value_n)
            [(+ value_y item_value) (conj set_y item_index)]
            no))))))
 
(def mm (memoize m))

(defn- scaling
  "Determins a scaling factore based on the knapsack capacity."
  [capacity]
  (inc (int (/ capacity 10000)))
  )

(defn- scale
  "Scales the weight of all candidate items by the scaling factor
  and rounding down to an integer value."
  [candidates scale_factor]
  (def scaled_candidates [])
  (doseq [item candidates]
    (def new_weight (int (/ (:weight item) scale_factor) ))
    (def new_item (assoc-in item [:weight]  new_weight))
    (def scaled_candidates (conj scaled_candidates new_item))
    )
  scaled_candidates
)

(defn pack
  "Given a vector of knapsack-item structs and a maximum capacity
  of the knapsack, returns the total value of the optimal knapsack
  content, and a vector of indices of items from the original
  vector that in the optimal carrying set."
  [candidates, cap]
  (def ^:dynamic scaled_candidates candidates)
  (def scale_factor (scaling cap))
  (if (> scale_factor 1)

    (let [ scaled_candidates (scale scaled_candidates scale_factor)]
      (def newcap (int (/ cap scale_factor)))
      (m scaled_candidates (-> candidates count dec) newcap)
       )
    
  (m candidates (-> candidates count dec) cap)
     
    )
  )

(defn weight
  "Given a vector of indicies and a vector of knapsack
  structs returns a sum of the weights of the items at the
  given indicies."
  [indices dolls]
  (def weights (map #( :weight (nth dolls %)) indices))
  (reduce #(+ %1 %2) weights)
  )

(defn names
  "Given a vector of indices and a vector of knapsack
  structs, returns a vector of strings of the name attributes
  for the items at the given indices."
  [indices dolls]
  (map #( :name (nth dolls %)) indices)
  )

(defn -main [& args]
    (def arg (first args))
    (if (or (= (count args) 0 ) (= "-h" arg))
      (println "usage: lein run <path to data file>")
      (let [[dolls, capacity] (doll-smuggler.read.file/get-data arg)]
        (def rv (pack dolls capacity))
        (println "dolls to smuggle:" (names (second rv) dolls))
        (println "     total value:" (first rv))
        (println "      max weight:" capacity)
        (println "    total weight:" (weight (second rv) dolls))
      )
    )
  )

;should output the indices or names and total weight
