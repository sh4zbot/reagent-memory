(ns reagent-memory.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(def se-es-cards
  [{:se "Hej" :es "Hola"}
   {:se "Arbeta" :es "Trabajando"}
   {:se "Apa" :es "Mono"}])

(defn create-state
  [word-pairs]
  {:cards word-pairs :current-card 0})

(def ratom
  (atom (create-state se-es-cards)))

(defn atom-next
  []
  (if (< (inc (get @ratom :current-card))
         (count (get @ratom :cards)))
    (swap! ratom update-in [:current-card] inc)))

(defn atom-prev
  []
  (if (> (get @ratom :current-card)
         0)
    (swap! ratom update-in [:current-card] dec)))

(defn card
  []
  (let [card-state (atom {:es-up true})]
    (fn []
      [:div
       [:section {:class "container"}
        [:div {:class (str "card " (:es-up @card-state))
               :on-click #(swap! card-state update-in [:es-up]
                                 (fn [es] (not es)))}
         [:div {:class "front"}
          (if (get @card-state :es-up)
            ""
            (:es (get-in @ratom [:cards (get @ratom :current-card)]))
            )]
         [:div {:class "back"}
          (if (get @card-state :es-up)
            (:se (get-in @ratom [:cards (get @ratom :current-card)]))
            "")]]]
       [:div
        [:button {:on-click #(swap! card-state update-in [:es-up]
                                    (fn [es] (not es)))} "Vänd"]
        [:button {:on-click #(do (atom-prev)
                                 (swap! card-state update-in [:es-up]
                                        (fn [es] true)))} "Foregaende"]
        [:button {:on-click #(do (atom-next)
                                 (swap! card-state update-in [:es-up]
                                        (fn [es] true)))} "Nasta"]]])))



;(defn card
;  []
;  (let [card-state (atom {:es-up true})]
;    (fn []
;      [:div {:class (if (get @card-state :es-up)
;                      "flip-container"
;                      "flip-container")}
;       [:p  (if (get @card-state :es-up)
;                (:es (get-in @ratom [:cards (get @ratom :current-card)]))
;                (:se (get-in @ratom [:cards (get @ratom :current-card)])))]
;
;       [:div
;        [:button {:on-click #(swap! card-state update-in [:es-up]
;                                    (fn [es] (not es)))} "Vänd"]
;        [:button {:on-click #(do (atom-prev)
;                                 (swap! card-state update-in [:es-up]
;                                        (fn [es] true)))} "Foregaende"]
;        [:button {:on-click #(atom-next)} "N'sta"]]])))



(defn ^:export main []
  (reagent/render [card]
                  (.getElementById js/document "app")))
(main)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
