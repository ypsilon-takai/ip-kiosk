(ns ip-kiosk.models.db
  (:require [clojure.java.jdbc :as s]
            [clj-time.local :as tl])
  (:import java.sql.DriverManager))


;; (def db {:classname "org.sqlite.JDBC",
;;          :subprotocol "sqlite",
;;          :subname "db.sq3"})

;; (def db {:classname "org.sqlite.JDBC",
;;          :subprotocol "sqlite",
;;          :subname ":memory:"})

(def db {:classname "org.h2.Driver"
         :subprotocol "h2:mem"
         :subname "ipdb;DB_CLOSE_DELAY=-1"
         :user "sa"
         :password ""
         })

;; sqlite
;; (defn create-host-table []
;;   (sql/db-do-commands
;;     db
;;    (sql/create-table-ddl
;;     :hostlist
;;     [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
;;     [:lastupdate "TEXT"]
;;     [:hostname "TEXT"]
;;     [:nickname "TEXT"]
;;     [:ip "TEXT"]
;;     [:contact "TEXT"]
;;     [:comment "TEXT"])))

;; h2
(defn create-host-table []
  (sql/db-do-commands
   db
   (sql/create-table-ddl
    :hostlist
    [:id "INTEGER PRIMARY KEY AUTO_INCREMENT"]
    [:lastupdate "varchar(30)"]
    [:hostname "varchar(50)"]
    [:nickname "varchar(50)"]
    [:ip "varchar(25)"]
    [:contact "varchar(50)"]
    [:comment "varchar(300)"])))


(defn- read-list []
  (sql/query db
             ["SELECT * FROM hostlist ORDER BY id ASC"]))

(defn host-name-list []
  (map :hostname
       (sql/query db
                  ["SELECT hostname FROM hostlist ORDER BY id ASC"])))

(defn- add-entry [content-map]
  (sql/insert!
   db
   :hostlist
   content-map))

(defn- update-entry [content-map]
  (sql/update!
   db
   :hostlist
   content-map
   ["hostname = ?" (:hostname content-map)]))

(defn- add-date-info [m]
  (assoc m :lastupdate (tl/format-local-time (tl/local-now) :mysql)))

;; public funcs
(defn insert-or-update [content-map]
  (if (some (partial = (:hostname content-map)) (host-name-list))
    (update-entry (add-date-info content-map))
    (add-entry (add-date-info content-map))))

(defn delete-entry [hostname]
  (if (some (partial = hostname) (host-name-list))
    (sql/delete! db :hostlist ["hostname = ?" hostname])))

(defn all-host-info []
  (map #(dissoc % :id)
       (read-list)))

