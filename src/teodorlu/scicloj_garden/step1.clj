(ns teodorlu.scicloj-garden.step1
  (:require
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

(ui/index {})
