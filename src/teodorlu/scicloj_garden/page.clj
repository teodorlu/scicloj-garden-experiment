(ns teodorlu.scicloj-garden.page)

;; Domain model only for pages.

(defn slug [page]
  (:page/slug page))

(defn uuid [page]
  (:page/uuid page))

(defn href [page]
  (str "/" (slug page) "/"))
