(ns wonderland.core
  (:gen-class) ; for -main method in uberjar
  (:require [wonderland.server :as server]))

(defn -main [& args] (server/-main))
