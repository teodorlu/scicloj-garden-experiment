(ns teodorlu.scicloj-garden.ui-test
  (:require
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]
   [teodorlu.bbmemex.pandoc :as pandoc]
   [teodorlu.scicloj-garden.ui :as ui]))

(deftest pandoc->hiccup-test--string
  ;; (pandoc/from-markdown "hei")
  (let [pandoc
        {:pandoc-api-version [1 23 1],
         :meta {},
         :blocks [{:t "Para", :c [{:t "Str", :c "hei"}]}]}]
    (is (= (list [:p "hei"])
           (ui/pandoc->hiccup pandoc)))))

(deftest pandoc->hiccup-test--with-space
  ;; (pandoc/from-markdown "hei _du_")
  (let [pandoc
        {:pandoc-api-version [1 23 1],
         :meta {},
         :blocks
         [{:t "Para", :c [{:t "Str", :c "hei"} {:t "Space"} {:t "Str", :c "du"}]}]}]
    (is (= (list [:p "hei" " " "du"])
           (ui/pandoc->hiccup pandoc)))))

(deftest pandoc->hiccup-test--with-space-and-emphasis
  ;; (pandoc/from-markdown "hei _du_")
  (let [pandoc
        {:pandoc-api-version [1 23 1],
         :meta {},
         :blocks
         [{:t "Para",
           :c
           [{:t "Str", :c "hei"} {:t "Space"} {:t "Emph", :c [{:t "Str", :c "du"}]}]}]}]
    (is (= (list [:p "hei" " " [:em "du"]])
           (ui/pandoc->hiccup pandoc)))))

(deftest pandoc->hiccup-test--heading
  ;; (pandoc/from-markdown "# a heading")
  (let [pandoc
        {:pandoc-api-version [1 23 1],
         :meta {},
         :blocks
         [{:t "Header",
           :c
           [1
            ["a-heading" [] []]
            [{:t "Str", :c "a"} {:t "Space"} {:t "Str", :c "heading"}]]}]}]
    (is (= (list [:h1 "a" " " "heading"])
           (ui/pandoc->hiccup pandoc)))))

(deftest pandoc->hiccup-test--whole-doc
  #_ (pandoc/from-markdown "
# Scicloj curriculum map (draft, experimental)

A paragraph.

A paragraph with _italic text_.
")
  (let [pandoc
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
            {:t "Str", :c "."}]}]}]
    (is (= (list [:h1 "Scicloj" " " "curriculum" " " "map" " " "(draft," " " "experimental)"]
                 [:p "A" " " "paragraph."]
                 [:p "A" " " "paragraph" " " "with" " " [:em "italic" " " "text"] "."])
           (ui/pandoc->hiccup pandoc)))))
