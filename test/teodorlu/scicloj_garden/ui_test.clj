(ns teodorlu.scicloj-garden.ui-test
  (:require
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]
   [teodorlu.bbmemex.pandoc :as pandoc]
   [teodorlu.scicloj-garden.ui :as ui]))

(comment
  ;; testing whether we can convert something really simple to html

  (pandoc/from-markdown "hei")
  {:pandoc-api-version [1 23 1],
   :meta {},
   :blocks [{:t "Para", :c [{:t "Str", :c "hei"}]}]}
  )

(deftest pandoc->hiccup-test
  (testing "Pandoc strings are strings in hiccup too"
    (let [pandoc
          {:pandoc-api-version [1 23 1],
           :meta {},
           :blocks [{:t "Para", :c [{:t "Str", :c "hei"}]}]}]
      (is (= (list [:p "hei"])
             (ui/pandoc->hiccup pandoc))))))

(comment
  ;; generate test data

  (def sample-markdown (str/trim "
# Scicloj curriculum map (draft, experimental)

A paragraph.

A paragraph with _italic text_.
"))

  (def sample-pandoc (pandoc/from-markdown sample-markdown))

  sample-pandoc
  {:pandoc-api-version [1 23 1],
   :meta {},
   :blocks
   [{:t "Header",
     :c
     [1
      ["scicloj-curriculum-map-draft-experimental" [] []]
      [{:t "Str", :c "Scicloj"}
       {:t "Space"}
       {:t "Str", :c "curriculum"}
       {:t "Space"}
       {:t "Str", :c "map"}
       {:t "Space"}
       {:t "Str", :c "(draft,"}
       {:t "Space"}
       {:t "Str", :c "experimental)"}]]}
    {:t "Para", :c [{:t "Str", :c "A"} {:t "Space"} {:t "Str", :c "paragraph."}]}
    {:t "Para",
     :c
     [{:t "Str", :c "A"}
      {:t "Space"}
      {:t "Str", :c "paragraph"}
      {:t "Space"}
      {:t "Str", :c "with"}
      {:t "Space"}
      {:t "Emph",
       :c [{:t "Str", :c "italic"} {:t "Space"} {:t "Str", :c "text"}]}
      {:t "Str", :c "."}]}]}

  (pandoc/from-markdown "hei")
  {:pandoc-api-version [1 23 1],
   :meta {},
   :blocks [{:t "Para", :c [{:t "Str", :c "hei"}]}]}

  (pandoc/from-markdown "hei du")
  {:pandoc-api-version [1 23 1],
   :meta {},
   :blocks
   [{:t "Para", :c [{:t "Str", :c "hei"} {:t "Space"} {:t "Str", :c "du"}]}]}

  (pandoc/from-markdown "hei _du_")
  {:pandoc-api-version [1 23 1],
   :meta {},
   :blocks
   [{:t "Para",
     :c
     [{:t "Str", :c "hei"} {:t "Space"} {:t "Emph", :c [{:t "Str", :c "du"}]}]}]}


  )
