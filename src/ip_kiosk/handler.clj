(ns ip-kiosk.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ip-kiosk.models.db :as db]
            [ip-kiosk.routes.if :as if]
            [clojure.tools.nrepl.server :as nrepl]))

;; (defn init []
;;   (println "Starting IP kiosk.")
;;   (if-not (.exists (java.io.File. "db.sq3"))
;;     (db/create-host-table)
;;     ))

(def nrepl-server (atom nil))

(defn init []
  (println "Starting IP kiosk.")
  (reset! nrepl-server (nrepl/start-server :port 7888))
  (db/create-host-table))

(defn destroy []
  (println "End.")
  (nrepl/stop-server @nrepl-server))

(defroutes app-routes
  (context "/sample" []
           (GET "/" [] (if/host-list-web))
           (POST "/api" [ipadd hostname comment nickname contact]
                 (do
                   (db/insert-or-update {:ip ipadd
                                         :hostname hostname
                                         :nickname nickname
                                         :comment comment
                                         :contact contact})
                   (str "IP:" ipadd
                        " hostname:" hostname
                        " comment:" comment
                        " nickname:" nickname
                        " contact:" contact)))
           (route/resources "/public/")
           (route/not-found "Not Found")))

(def app
  (handler/site app-routes))

