(ns reagent-memory.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def se-es-pairs
  [{:se "Hej" :es "Hola"}
   {:se "Arbeta" :es "Trabajando"}
   {:se "Apa" :es "Mono"}])

(defn card [card-pair-id text]
  "Creates a card with the given card pair id and text"
  (let [card-state (atom {:card-pair-id card-pair-id :text text :visible true})]
    (fn []
      [:div {:class "container"}
       [:div {:class    (str "card " (:visible @card-state))
              :on-click #(swap! card-state update-in [:visible] (fn [b] (not b)))}
        [:div {:class "front"}
         (get @card-state :text)]
        [:div {:class "back"}]]])))


(defn create-cards [wordpairs]
  "Takes pairs of words and returns a list with the paired words separated
   but with the same card-pair-id"
  (loop [card-pair-id 1
         finished []
         start wordpairs]
    (if (empty? start)
      finished
      (recur (inc card-pair-id)
             (concat finished
                     [(card card-pair-id (:se (first start)))
                      (card card-pair-id (:es (first start)))])
             (rest start)))))

(defn board [cards]
  [:div
   (for [item cards]
     (item))])

(defn ^:export main []
  (reagent/render [board (create-cards se-es-pairs)]
                  (.getElementById js/document "app")))
(main)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
