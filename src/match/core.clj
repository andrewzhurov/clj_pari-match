(ns match.core
  (:require [clojure.test :refer [is testing deftest run-tests]]))

(defn to-numbers [score]
  (let [out  [(re-find #"^[\d-]+" score) (re-find #"[\d-]+$" score)]]
    (if (= out ["-" "-"])
      [-1 -1]
      (vec (map #(Integer/parseInt %) out)))))

;; underneath used values [-1 -1] for a draw
(defn draw? [f s]
  (and (= f s)
       (apply (partial = -1) f)))

(defn match? [cli real]
  (= cli real))

;; Exact match of winning side doesn't matter
(defn correct-winner? [cli real]
  (->> [cli real]
       (map (fn [[fir sec]] (if (> fir sec) :first :second)))
       (apply =)))

(defn pari [cli-score real-score] 
  (let [[cli real] (map to-numbers [cli-score real-score])]
    (cond
     (draw? cli real) 1
     (match? cli real) 2
     (correct-winner? cli real) 1
     :else 0)))

(deftest to-numbers-test
  (testing "all"
    (is (and (= (to-numbers "2:0") [2 0])
             (= (to-numbers "11:222") [11 222])
             (= (to-numbers "-:-") [-1 -1])))))
(deftest pari-test
  (testing "perfect match"
    (is (= (pari "2:0" "2:0") 2))
    (is (= (pari "2:2" "2:2") 2))
    (is (= (pari "0:0" "0:0") 2)))
  (testing "correct winner"
    (is (= (pari "3:4" "2:5") 1))
    (is (= (pari "3:0" "2:1") 1)))
  (testing "draw"
    (is (= (pari "-:-" "-:-") 1)))
  (testing "loss"
    (is (= (pari "3:2" "3:4") 0)
        (= (pari "5:6" "6:6") 0))))

(run-tests)
