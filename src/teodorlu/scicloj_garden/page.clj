(ns teodorlu.scicloj-garden.page
  (:require
   [babashka.fs :as fs]
   [clojure.edn :as edn]
   hiccup.page
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

(defn from-edn-file [edn-file]
  (assoc (edn/read-string (slurp (fs/file edn-file)))
         :page/slug (str (fs/parent edn-file))))

(defn from-slug [slug]
  (from-edn-file (fs/file slug "page.edn")))

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
       (empty? (fs/modified-since (content-html-file page)
                                  [(content-markdown-file page)
                                   (fs/file "page-generator-version.txt")]))))

(defn force-rebuild! [page]
  (when (has-content-markdown? page)
    (when-let [html (or (some-> (content-markdown page)
                                (pandoc/from-markdown)
                                ui/pandoc->hiccup
                                hiccup.page/html5))]
      (spit (content-html-file page) (str html "\n"))
      ::page-rebuild-complete)))

(defn rebuild! [page]
  (when-not (content-html-fresh? page)
    (force-rebuild! page)))

(comment

  (def mypage (from-slug "scicloj-curmap-draft"))

  (content-html-fresh? mypage)

  (fs/modified-since (content-html-file mypage)
                     [(fs/file "page-generator-version.txt")])

  (fs/last-modified-time (content-html-file mypage))
  ;; => #object[java.nio.file.attribute.FileTime 0x59e639b8 "2024-04-07T15:23:58.426474435Z"]

  (fs/last-modified-time (fs/file "page-generator-version.txt"))
  ;; => #object[java.nio.file.attribute.FileTime 0x57b44f9 "2024-04-07T15:24:31.385074815Z"]


  mypage

  (rebuild! mypage)
  (force-rebuild! mypage)

  (has-content-markdown? mypage)



  :rcf)

(comment
  (content-html-fresh? (from-edn-file (first (fs/glob "." "*/page.edn")))))


(comment

  (def f "file.txt")

  (spit f (str "A number: " (rand)))

  (fs/last-modified-time f)
  ;; => #object[java.nio.file.attribute.FileTime 0x4fdb7fc "2024-04-07T15:38:21.857339182Z"]

  (spit f (str "A number: " (rand)))

  (fs/last-modified-time f)
  ;; => #object[java.nio.file.attribute.FileTime 0x55da8faa "2024-04-07T15:39:07.135860486Z"]

  ;; writes if it changes.
  ;; OK.

  ;; but what if it does not change?

  ;; is my operating system messing this up or what?

  (def f2 "file2.txt")
  (def content2 "file twwwooooo")

  (spit f2 content2)

  (fs/last-modified-time f2)
  ;; => #object[java.nio.file.attribute.FileTime 0x4c1613e1 "2024-04-07T15:40:43.791278077Z"]

  ;; now, do it again.

  (spit f2 content2)
  (fs/last-modified-time f2)
  ;; => #object[java.nio.file.attribute.FileTime 0x7609ffe "2024-04-07T15:41:13.065802269Z"]

  ;; and again.
  (spit f2 content2)
  (fs/last-modified-time f2)
  ;; => #object[java.nio.file.attribute.FileTime 0x365a2bc7 "2024-04-07T15:41:45.390311086Z"]

  ;; this time i'm unable to reproduce.
  ;; this is weird.

  :rcf)
