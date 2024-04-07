(ns teodorlu.scicloj-garden.ui)

(defn index [{}]
  [:html [:body
          [:h1 "Scicloj knowledge garden: a memex?"]
          [:p "We'll see!"]
          [:p "Source: "
           [:a {:href "https://github.com/teodorlu/scicloj-garden-experiment"}
            "github.com/teodorlu/scicloj-garden-experiment"]]]])
