(defproject wonderland "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [
                 [io.pedestal/pedestal.service       "0.5.8"]
                 [io.pedestal/pedestal.jetty         "0.5.8"]
                 [io.pedestal/pedestal.immutant      "0.5.8"]
                 [io.pedestal/pedestal.tomcat        "0.5.8"]
                 [ns-tracker "0.2.2"]
                 [org.clojure/clojure "1.10.1"]]
  :repl-options {:init-ns wonderland.server}
  :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "wonderland.server/run-dev"]}
                    :dependencies [[io.pedestal/pedestal.service-tools "0.5.5"]]}}
  :main wonderland.core)
