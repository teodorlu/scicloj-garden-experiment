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

        (= "Space" (:t el))
        " "

        (= "Emph" (:t el))
        (into [:em] (map pandoc-el->hiccup (:c el)))

        (= "Header" (:t el))
        (let [header-tag (get {1 :h1 2 :h2 3 :h3 4 :h4 5 :h5 6 :h6}
                              (first (:c el)))]
          (into [header-tag] (map pandoc-el->hiccup (get (:c el) 2))))

        :else nil))

(defn pandoc->hiccup [pandoc]
  (apply list (map pandoc-el->hiccup (:blocks pandoc))))
