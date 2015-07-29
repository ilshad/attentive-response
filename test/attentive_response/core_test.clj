(ns attentive-response.core-test
  (:require [clojure.test :refer :all]
            [attentive-response.core :refer :all]))

(defn view [_] {:body {:foo [:bar 42]}})

(defn another-middleware-updating-headers [handler]
  (fn [request]
    (assoc (handler request) :headers {"Access-Control-Allow-Origin" "*"})))

(def handler
  (-> view
      another-middleware-updating-headers
      wrap-attentive-response))

(deftest test-middleware
  (testing
    (is (= (handler {:headers {"accept" "text/html, text/plain; q=0.8"}})
            {:body "{\"foo\":[\"bar\",42]}"
             :headers {"Access-Control-Allow-Origin" "*"
                       "Content-Type" "application/json; charset=utf-8"}}))
    (is (= (handler {:headers {"accept" "text/html, application/edn"}})
           {:body "{:foo [:bar 42]}"
            :headers {"Access-Control-Allow-Origin" "*"
                      "Content-Type" "application/edn; charset=utf-8"}}))
    (is (= (handler {:headers {"accept" "text/html, application/transit+json"}})
           {:body "[\"^ \",\"~:foo\",[\"~:bar\",42]]"
            :headers {"Access-Control-Allow-Origin" "*"
                      "Content-Type" "application/transit+json; charset=utf-8"}}))))

