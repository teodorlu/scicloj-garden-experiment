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
       (map page/edn-file->slug+meta)))

(comment
  (pages)


  )

(defn build! []
  (spit "index.html"
        (hiccup.page/html5 (ui/index {}))))

(defn clean! []
  (let [build-artifacts #{"index.html"}]
    (doseq [f build-artifacts]
      (fs/delete-if-exists f))))

(comment
  (build!)
  (clean!)

  :rcf)
