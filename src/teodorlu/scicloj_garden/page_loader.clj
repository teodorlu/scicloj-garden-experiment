(ns teodorlu.scicloj-garden.page-loader
  (:require
   [babashka.fs :as fs]
   [clojure.edn :as edn]
   hiccup.page
   [teodorlu.bbmemex.pandoc :as pandoc]
   [teodorlu.scicloj-garden.page :as page]
   [teodorlu.scicloj-garden.pandoc2hiccup :as pandoc2hiccup]
   [teodorlu.scicloj-garden.ui :as ui]))

;; TODO
;;
;; Split this into `page` and `page-loader`.
;; `page-loader` depends on `page`.
;; `page` is domain only.

;; terminology
;;
;; a PAGE has a slug and an UUID.
;; its slug and its uuid is a subset of the page METADATA.
;;
;; a page MAY contain content.
;;
;; if the content is given in markdown, the content is stored in index.md.
;; if the content is given in html, the content is stored in index.html.

(defn from-edn-file [edn-file]
  (assoc (edn/read-string (slurp (fs/file edn-file)))
         :page/slug (str (fs/parent edn-file))))

(defn from-slug [slug]
  (from-edn-file (fs/file slug "page.edn")))

(defn uuid [page]
  (:page/uuid page))

(defn content-markdown-file [page]
  (fs/file (page/slug page) "index.md"))

(defn has-content-markdown? [page]
  (fs/exists? (content-markdown-file page)))

(defn content-markdown
  "The markdown content of a page, if such content exists."
  [page]
  (when (has-content-markdown? page)
    (slurp (content-markdown-file page))))

(defn content-html-file [page]
  (fs/file (page/slug page) "index.html"))

(defn has-content-html? [page]
  (fs/exists? (content-html-file page)))

(comment
  (fs/modified-since "index.html" #{"deps.edn"}))

(defn content-html-fresh? [page]
  (and (has-content-markdown? page)
       (has-content-html? page)
       (empty? (fs/modified-since (content-html-file page)
                                  [(content-markdown-file page)
                                   (fs/file "page-generator-version.txt")]))))

(defn force-rebuild! [page]
  (when (has-content-markdown? page)
    (when-let [html (some-> (content-markdown page)
                            (pandoc/from-markdown)
                            pandoc2hiccup/pandoc->hiccup
                            hiccup.page/html5)]
      (spit (content-html-file page) (str html "\n"))
      ::page-rebuild-complete)))

(defn rebuild! [page]
  (when-not (content-html-fresh? page)
    (force-rebuild! page)))

(comment

  (def mypage (from-slug "scicloj-curmap-draft"))
  mypage

  (rebuild! mypage)
  (force-rebuild! mypage)

  (has-content-markdown? mypage)

  (content-html-fresh? mypage)

  :rcf)
