(ns wonderland.server
  (:gen-class) ; for -main method in uberjar
  (:require [io.pedestal.http :as http]
            [ns-tracker.core :refer [ns-tracker]]
            [io.pedestal.http.route :as route :refer [router]]
            [wonderland.service :as service]))

(defonce runnable-service (http/create-server service/service))

(defn foo
  "I don't do a whole lot."
  [x]
  (recur x))

(defn fact [x]
  (if (= x 1) 1 (* x (fact (- x 1)))))

(defn factorial-using-recur [n]
  (loop [current n
         next (dec current)
         total 1]
    (if (> current 1)
      (recur next (dec next) (* total current))
      total)))

(defn run-dev
  "The entry-point for 'lein run-dev'"
  [& args]
  (println "\nCreating your [DEV] server...")
  (-> service/service ;; start with production configuration
      (merge {:env :dev
              ;; do not block thread that starts web server
              ::http/join? false
              ;; Routes can be a function that resolve routes,
              ;;  we can use this to set the routes to be reloadable
              ::http/routes #(deref #'service/routes)
              ;; all origins are allowed in dev mode
              ::http/allowed-origins {:creds true :allowed-origins (constantly true)}})
      ;; Wire up interceptor chains
      http/default-interceptors
      http/dev-interceptors
      http/create-server
      http/start))

(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (println "\nCreating your server...")
  (http/start runnable-service))
