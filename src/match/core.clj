(ns match.core)

;; I think that's the wrong way. And here should be special value for a draw.
;; Since f=s is a perfect match and it deserves 2 points.
;; (Easy to realize by using agreement that [-1 -1] is a draw.)
(defn draw? [f s]
  (and (= f s)
       (= (first f) (second f))))

(defn match? [cli real]
  (= cli real))

;; If we forget about our contract with draws we could be really surprised by [2 3] [3 3],
;; for example, since it matches. So, don't forget about it ;)
(defn correct-win? [cli real]
  (->> [cli real]
       (map (fn [[fir sec]] (if (> fir sec) :first :second)))
       (apply =)))

(defn pari [cli real]
   (cond
     (draw? cli real) 1
     (match? cli real) 2
     (correct-win? cli real) 1
     :else 0))
