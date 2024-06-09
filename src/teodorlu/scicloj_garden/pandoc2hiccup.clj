(ns teodorlu.scicloj-garden.pandoc2hiccup
  (:require
   pandocir.hiccup
   pandocir.ir
   clojure.walk))

(defn ^:private strip-hiccup-header-attrs [hiccup]
  (let [hiccup-node? (fn [node]
                       (and (vector? node)
                            (keyword? (first node))))
        strip-attrs (fn [[tag maybe-attrs & children]]
                      (if (map? maybe-attrs)
                        (into [tag] children)
                        (into [tag maybe-attrs] children)))]
    (clojure.walk/postwalk (fn [node]
                             (if (hiccup-node? node)
                               (strip-attrs node)
                               node))
                           hiccup)))

(defn ^:private ir->hiccup [ir]
  (->> ir
       pandocir.hiccup/ir->hiccup
       :blocks
       (apply list)
       strip-hiccup-header-attrs))

(defn ^:private pandoc->hiccup-new [pandoc]
  (-> pandoc
      pandocir.ir/pandoc->ir
      ir->hiccup))

(defn pandoc->hiccup [pandoc]
  (pandoc->hiccup-new pandoc))
