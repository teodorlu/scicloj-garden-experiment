(ns teodorlu.scicloj-garden.page
  (:require
   [babashka.fs :as fs]
   [clojure.edn :as edn]
   [clojure.string :as str]))

(defn edn-file->page [edn-file]
  (assoc (edn/read-string (slurp (fs/file edn-file)))
         :page/slug (str (fs/parent edn-file))))

(defn slug [page]
  (:page/slug page))

(defn uuid [page]
  (:page/uuid page))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;,,

(defn doc-markdown [page]
  (let [md-path (fs/file (slug page) "index.md")]
    (when (fs/exists? md-path)
      (slurp md-path))))
