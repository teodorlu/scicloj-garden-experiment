(ns teodorlu.scicloj-garden.step1
  (:require
   [babashka.fs :as fs]
   [hiccup.page]
   [teodorlu.scicloj-garden.page :as page]
   [teodorlu.scicloj-garden.ui :as ui]))

;; no organization just yet, just try to get started
;; not get stuck in analysis paralysis

;; for now, THIS NS is everything, we drive everything from the REPL.

;; TODO
;;
;; - Build an index.html for the currmap page
;; - Build a toplevel index.html
;;
;; NICE TO HAVE
;;
;; - Make it fast
;;   - Pure babashka / JVM
;;   - Avoid rework (when possible)
;; - Avoid using Pandoc for the IR to HTML step

(comment
  (->> (fs/glob "." "*/page.edn")
       (map fs/parent)
       (map str)))

(defn pages
  "Traverse the files to find the pages"
  []
  (->> (fs/glob "." "**/page.edn")
       (map page/from-edn-file)))

(comment
  (pages)
  (page/content-markdown (first (pages)))
  )

(defn rebuild-index! []
  (spit "index.html"
        (hiccup.page/html5 (ui/index {}))))

(comment
  (rebuild-index!))

(comment
  (def p (first (pages)))

  (page/content-markdown p)

  :rcf)

(defn rebuild-page! [page]
  (let [status (page/rebuild! page)]
    (when (= status ::page/page-rebuild-complete)
      (println "Rebuilt" (page/slug page)))))

(defn rebuild!
  "Rebuilds everything -- no caching behavior."
  []
  (rebuild-index!)
  (doseq [page (pages)]
    (rebuild-page! page)))

(defn clean! []
  (fs/delete-if-exists "index.html")
  (doseq [page (pages)]
    (fs/delete-if-exists (page/content-html-file page))))

(comment
  (rebuild!)
  (clean!)

  (:page/slug (first (pages)))
  ;; => "scicloj-curmap-draft"

  :rcf)
