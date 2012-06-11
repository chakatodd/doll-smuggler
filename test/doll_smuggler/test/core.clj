(ns doll-smuggler.test.core
  (:use [doll-smuggler.core])
  (:use [doll-smuggler.test.read.file])
  (:use [clojure.test]))

(def test-data
  [ "map"         9 150
    "compass"    13  35
    "water"     153 200
    "sandwich"   50 160
    "glucose"    15  60
    "towel"      18  12
    "socks"       4  50
    "book"       30  10])
 
(defstruct item :name :weight :value)
 
(def test_items (vec (map #(apply struct item %) (partition 3 test-data))))

(deftest test_trivial
  (is (= 0 (count (second (pack test_items 0)))))
  (is (= 0 (count (second (pack [] 400)))))
  (is (= 0 (count (second (pack [] 0))))))


(defn optimal_set
  [candidate_set]
  (def optimal (filter #(= 1 (:optimal %)) candidate_set))
  (sort (map #(.indexOf candidate_set %) optimal)))



(defn testfile
  [file]
  (let [[dolls, capacity] (doll-smuggler.test.read.file/get-data file)]
    (def test_rv (pack dolls capacity))
    (def inv (second test_rv))
    (def optimal (optimal_set dolls))
    (is (= inv optimal) (str "optimal result found for: " file))))

(deftest data
  (def datadir "test/doll_smuggler/test/data/")
  (doseq [file ["mule.txt" "p01.txt" "p02.txt" "p03.txt" "p04.txt"
                "p05.txt" "p06.txt" "p07.txt" "p08.txt" "p09.txt" "p10.txt"]]
    (testfile (str datadir file))))

