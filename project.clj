(defproject ip-kiosk "1.0.0"
  :description "IP address information kiosk"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/java.jdbc "0.3.5"]
                 [clj-time "0.8.0"]
                 [javax.servlet/servlet-api "2.5"]
                 [compojure "1.1.8"]
                 [hiccup "1.0.5"]
                 [cheshire "5.3.1"]
                 [com.h2database/h2 "1.4.178"]
                 [org.clojure/tools.nrepl "0.2.5"]]
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler ip-kiosk.handler/app
         :init ip-kiosk.handler/init
         :destroy ip-kiosk.handler/destroy
         :servlet-path-info? false
         :uberwar-name "ip-kiosk.war"
         }
  :resource-paths ["resources"]
  :war-resource-paths ["resources"]
  :profiles
  {:dev {:dependencies [[ring-server "0.3.0"]
                        [ring-mock "0.1.5"]
                        [ring/ring-devel "1.2.0"]]}})
