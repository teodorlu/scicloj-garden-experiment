(ns teodorlu.scicloj-garden.pandocir-test
  (:require  [clojure.test :refer [deftest is]]))

;; I'm writing some test cases to see if I can understand how teodorlu/pandocir works now.

(deftest two
  (is (= 2 (+ 1 1)))
  (is (= 3 (+ 1 1 1))))
