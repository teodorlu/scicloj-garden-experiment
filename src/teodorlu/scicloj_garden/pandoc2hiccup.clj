(ns teodorlu.scicloj-garden.pandoc2hiccup
  (:require
   pandocir.hiccup
   pandocir.ir))

(defn ^:private pandoc-el->hiccup-old [el]
  (cond (= "Para" (:t el))
        (into [:p] (map pandoc-el->hiccup-old (:c el)))

        (= "Str" (:t el))
        (:c el)

        (= "Space" (:t el))
        " "

        (= "Emph" (:t el))
        (into [:em] (map pandoc-el->hiccup-old (:c el)))

        (= "Header" (:t el))
        (let [header-tag (get {1 :h1 2 :h2 3 :h3 4 :h4 5 :h5 6 :h6}
                              (first (:c el)))]
          (into [header-tag] (map pandoc-el->hiccup-old (get (:c el) 2))))

        :else nil))

(defn ^:private pandoc->hiccup-old [pandoc]
  (apply list (map pandoc-el->hiccup-old (:blocks pandoc))))

(defn ^:private ir->hiccup [ir]
  (->> ir
       pandocir.hiccup/ir->hiccup
       :blocks
       (apply list)))

(defn ^:private pandoc->hiccup-new [pandoc]
  (-> pandoc
      pandocir.ir/pandoc->ir
      ir->hiccup))

(defn pandoc->hiccup [pandoc]
  (pandoc->hiccup-old pandoc))
