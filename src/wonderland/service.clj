(ns wonderland.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [ns-tracker.core :refer [ns-tracker]]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route :as route :refer [router]]
            [io.pedestal.http.route.definition :refer [defroutes]]))

(defn hello-world
  [request]
  (let [name (get-in request [:params :name] "World")]
    {:status 200 :body (str "Hello " name "!\n")}))

(defn is-divisible [num divisor]
  (= 0 (mod num divisor)))

(defn is-prime [num]
  (loop [num num current 2]
    (cond
      (= current num) true
      (is-divisible num current) false
      :else (recur num (+ 1 current)))))

(defn remover [num coll]
  (vec (remove #(= %1 num) coll)))

(def starting-point [1 2 3 4 5 6 7 8 9])

 (vec (remove #(= 0 %1) (take 9 (range))))

(defn find-first
  [f coll]
  (first (filter f coll)))

(defn form-trie
  ([numlist] (form-trie numlist {}))
  ([numlist trie]
   (cond
     (= numlist []) trie
     :else (reduce
             #(assoc %1 (str %2) (form-trie (remover %2 numlist)))
             {}
             numlist))))

(defn extract-from-trie
  ([trie] (vec (flatten (map #(extract-from-trie (%1 0) (%1 1)) (seq trie)))))
  ([num trie]
   (cond
     (= trie {}) (Integer/parseInt num)
     :else (map #(extract-from-trie (str num (%1 0)) (%1 1)) (seq trie)))))

(defn find-pan-prime [numvec]
 (let [pans (sort > (extract-from-trie (form-trie numvec)))]
   (find-first is-prime pans)))

(defn find-largest-pan-prime [& args]
  (loop [x [1 2 3 4 5 6 7 8 9]]
    (let [pan-prime (find-pan-prime x)]
      (if pan-prime pan-prime (recur (pop x))))))

(defn pan-digital
  [request]
  (let [name (get-in request [:params :name] "World")]
    {:status 200 :body (str "ok: " (find-largest-pan-prime))}))

(defroutes routes
  [[["/"
      ["/hello" {:get hello-world}]
      ["/pan-digital" {:get pan-digital}]
      ]]])

(def modified-namespaces (ns-tracker "src"))

(def service {:env                 :prod
              ::http/routes        routes
              ::http/resource-path "/public"
              ::http/type          :jetty
              ::http/port          8080
              ::http/interceptors [(router (fn []
                                     (doseq [ns-sym (modified-namespaces)]
                                       (require ns-sym :reload))
                                     routes))]
              })
