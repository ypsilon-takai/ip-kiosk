(ns ip-kiosk.routes.if
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [hiccup.form :refer :all]
            [ip-kiosk.models.db :as db]
            [hiccup.page :refer [html5 include-css]]))

(defn common-layout [& body]
  (html5
    [:head
     [:title "NSN Server IP information"]
;;     (include-css "/ip-kiosk/public/css/style.css")
     (include-css "/sample/public/css/style.css")
     ]
    [:body
     [:div {:id "wrapper"}
      [:div {:id "header"}
       [:div {:id "logo"}]]
      [:div {:id "content"}
       body]
      [:div {:id "footer"}
       "Copyright ©2014- Nokia Japan. All rights reserved."]]]))


(defn host-list-web []
  (let [host-list (db/all-host-info)]
    (common-layout
     [:table {:id "list"}
      [:caption {:align "left" :style "text-align:left;"} "IP list"]
      [:tr
       [:th "Last update"]
       [:th "Nickname"]
       [:th "Host name"]
       [:th "IP address"]
       [:th "Contact"]
       [:th "Note"]]
      (for [[host row] (map #(-> [%1 %2]) host-list (range))]
        [:tr (if (odd? row) {:class "bgBlue"})
         [:td (:lastupdate host)]
         [:td (:nickname host)]
         [:td (:hostname host)]
         [:td (:ip host)]
         [:td (:contact host)]
         [:td (:comment host)]])])))


