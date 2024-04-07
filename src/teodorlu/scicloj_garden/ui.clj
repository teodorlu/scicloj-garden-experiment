(ns teodorlu.scicloj-garden.ui)

(defn index [{}]
  (let [title "Scicloj knowledge garden: a memex?"]
    (list
     [:head [:title title]]
     [:body
      [:h1 title]
      [:p "We'll see!"]
      [:p "Source: "
       [:a {:href "https://github.com/teodorlu/scicloj-garden-experiment"}
        "github.com/teodorlu/scicloj-garden-experiment"]]])))

(defn pandoc-el->hiccup [el]
  (cond (= "Para" (:t el))
        (into [:p] (map pandoc-el->hiccup (:c el)))

        (= "Str" (:t el))
        (:c el)

        :else nil))

(defn pandoc->hiccup [pandoc]
  (into (list) (map pandoc-el->hiccup (:blocks pandoc))))
