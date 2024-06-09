(ns teodorlu.scicloj-garden.ui
  (:require
   clojure.walk
   [teodorlu.scicloj-garden.page :as page]))

(defn page-list-item [page]
  [:li [:a {:href (str "/" (page/slug page)"/")}
        (page/slug page)]])

(defn index [{:keys [pages]}]
  (let [title "Scicloj knowledge garden: a memex?"]
    (list
     [:head [:title title]]
     [:body
      [:h1 title]
      [:p "We'll see!"]
      [:p "Source: "
       [:a {:href "https://github.com/teodorlu/scicloj-garden-experiment"}
        "github.com/teodorlu/scicloj-garden-experiment"]]
      [:p "Pages:"]
      [:ul (for [p (sort-by page/slug pages)]
             (page-list-item p))]])))
