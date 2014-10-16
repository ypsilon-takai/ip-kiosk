(ns ip-kiosk.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ip-kiosk.models.db :as db]
            [ip-kiosk.routes.if :as if]
            [clojure.tools.nrepl.server :as nrepl]
            [cheshire.core :as json]))

(def nrepl-server (atom nil))

(defn init []
  (println "Starting IP kiosk.")
  (reset! nrepl-server (nrepl/start-server :port 7888))
  (db/create-host-table))

(defn destroy []
  (println "End.")
  (nrepl/stop-server @nrepl-server))

(defroutes app-routes
  (context "/ip-kiosk" []
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
           (GET "/api" [name]
                (-> (db/get-host-info name)
                    (json/generate-string ,,)))
           (route/resources "/public/")
           (route/not-found "Not Found")))

(def app
  (handler/site app-routes))

