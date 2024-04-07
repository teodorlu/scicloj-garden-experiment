(ns teodorlu.scicloj-garden.page
  (:require
   [babashka.fs :as fs]
   [clojure.edn :as edn]))

(defn edn-file->slug+meta [edn-file]
  (assoc (edn/read-string (slurp (str edn-file)))
         :page/slug (str (fs/parent edn-file))))
