(ns teodorlu.scicloj-garden.page
  (:require
   [babashka.fs :as fs]
   [clojure.edn :as edn]
   [hiccup2.core :as hiccup]
   [teodorlu.bbmemex.pandoc :as pandoc]
   [teodorlu.scicloj-garden.ui :as ui]))

;; terminology
;;
;; a PAGE has a slug and an UUID.
;; its slug and its uuid is a subset of the page METADATA.
;;
;; a page MAY contain content.
;;
;; if the content is given in markdown, the content is stored in index.md.
;; if the content is given in html, the content is stored in index.html.

(defn edn-file->page [edn-file]
  (assoc (edn/read-string (slurp (fs/file edn-file)))
         :page/slug (str (fs/parent edn-file))))

(defn slug [page]
  (:page/slug page))

(defn uuid [page]
  (:page/uuid page))

(defn content-markdown-file [page]
  (fs/file (slug page) "index.md"))

(defn has-content-markdown? [page]
  (fs/exists? (content-markdown-file page)))

(defn content-markdown
  "The markdown content of a page, if such content exists."
  [page]
  (when (has-content-markdown? page)
    (slurp (content-markdown-file page))))

(defn content-html-file [page]
  (fs/file (slug page) "index.html"))

(defn has-content-html? [page]
  (fs/exists? (content-html-file page)))

(comment
  (fs/modified-since "index.html" #{"deps.edn"}))

(defn content-html-fresh? [page]
  (and (has-content-markdown? page)
       (has-content-html? page)
       (seq (fs/modified-since (content-html-file page)
                               (set (content-markdown-file page))))))


(defn force-rebuild! [page]
  (when (has-content-markdown? page)
    (when-let [html (some-> (content-markdown page)
                            (pandoc/from-markdown)
                            ui/pandoc->hiccup
                            hiccup/html)]
      (spit (content-html-file page) html)
      ::page-rebuild-complete)))

(defn rebuild! [page]
  (when-not (content-html-fresh? page)
    (force-rebuild! page)))

(comment
  (content-html-fresh? (edn-file->page (first (fs/glob "." "*/page.edn")))))
