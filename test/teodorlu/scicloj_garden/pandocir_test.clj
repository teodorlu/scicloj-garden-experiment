(ns teodorlu.scicloj-garden.pandocir-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [clojure.string :as str]
   [teodorlu.bbmemex.pandoc :as pandoc]
   [pandocir.ir]
   [pandocir.hiccup]))

;; I'm writing some test cases to see if I can understand how teodorlu/pandocir works now.

(deftest two
  (is (= 2 (+ 1 1)))
  (is (= 3 (+ 1 1 1))))

#_
(-> "heart of clojure *yay*"
    pandoc/from-markdown)


(deftest markdown-test
  (testing "markdown-> output looks like sane pandoc json"
    (let [markdown "hei\npå deg!"]
      (is (map? (pandoc/from-markdown markdown)))
      (is (contains? (pandoc/from-markdown markdown)
                     :pandoc-api-version))))
  (testing "we can roundtrip from and to markdown"
    (let [markdown "hei\n\npå deg!\n"]
      (is (= markdown
             (-> markdown
                 pandoc/from-markdown
                 pandoc/to-markdown)))))
  (testing "but roundtripping only works exactly with a single trailing newline"
    (let [markdown-no-newline "hei\n\npå deg!"]
      (is (not= markdown-no-newline
                (-> markdown-no-newline
                    pandoc/from-markdown
                    pandoc/to-markdown))))
    (let [markdown-two-newlines "hei\n\npå deg!\n\n"]
      (is (not= markdown-two-newlines
                (-> markdown-two-newlines
                    pandoc/from-markdown
                    pandoc/to-markdown))))))

(deftest convert-test
  (is (= "<p><em>teodor</em></p>" (-> "_teodor_" pandoc/from-markdown pandoc/to-html str/trim))))

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
