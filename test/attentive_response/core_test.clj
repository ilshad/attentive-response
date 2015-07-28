(ns attentive-response.core-test
  (:require [clojure.test :refer :all]
            [attentive-response.core :refer :all]))

(defn view [_] {:body {:foo [:bar 42]}})

(def handler (wrap-attentive-response view))

(deftest test-middleware
  (testing
    (is (= (handler {:headers {"accept" "text/html, text/plain; q=0.8"}})
            {:body "{\"foo\":[\"bar\",42]}"
             :headers {"Content-Type" "application/json; charset=utf-8"}}))
    (is (= (handler {:headers {"accept" "text/html, application/edn"}})
           {:body "{:foo [:bar 42]}"
            :headers {"Content-Type" "application/edn; charset=utf-8"}}))
    (is (= (handler {:headers {"accept" "text/html, application/transit+json"}})
           {:body "[\"^ \",\"~:foo\",[\"~:bar\",42]]"
            :headers {"Content-Type" "application/transit+json; charset=utf-8"}}))))

