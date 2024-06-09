(ns teodorlu.scicloj-garden.pandocir-test
  (:require
   [clojure.test :refer [deftest is]]
   [teodorlu.bbmemex.pandoc :as pandoc]
   [pandocir.ir]
   [pandocir.hiccup]))

(def heart-of-clojure-yay-raw-pandoc
  {:pandoc-api-version [1 23 1],
   :meta {},
   :blocks
   [{:t "Para",
     :c
     [{:t "Str", :c "heart"}
      {:t "Space"}
      {:t "Str", :c "of"}
      {:t "Space"}
      {:t "Str", :c "clojure"}
      {:t "Space"}
      {:t "Emph", :c [{:t "Str", :c "yay"}]}]}]})

(deftest pandoc-parse-markdown-test
  (is (= (-> "heart of clojure *yay*" pandoc/from-markdown)
         heart-of-clojure-yay-raw-pandoc)))

(def heart-of-clojure-yay-pandocir
  {:pandoc-api-version [1 23 1],
   :meta {},
   :blocks
   [{:pandocir/type :pandocir.type/para,
     :pandocir/inlines
     [{:pandocir/type :pandocir.type/str, :pandocir/text "heart"}
      {:pandocir/type :pandocir.type/space}
      {:pandocir/type :pandocir.type/str, :pandocir/text "of"}
      {:pandocir/type :pandocir.type/space}
      {:pandocir/type :pandocir.type/str, :pandocir/text "clojure"}
      {:pandocir/type :pandocir.type/space}
      {:pandocir/type :pandocir.type/emph,
       :pandocir/inlines
       [{:pandocir/type :pandocir.type/str, :pandocir/text "yay"}]}]}]})

(deftest pandocir-test
  (is (= heart-of-clojure-yay-pandocir
         (-> heart-of-clojure-yay-raw-pandoc pandocir.ir/pandoc->ir))))

(defn ir->hiccup [ir]
  (->> ir
       pandocir.hiccup/ir->hiccup
       :blocks
       (apply list)))

(deftest ir->hiccup-test
  (is (= '([:p "heart" " " "of" " " "clojure" " " [:em "yay"]])
         (ir->hiccup heart-of-clojure-yay-pandocir))))
