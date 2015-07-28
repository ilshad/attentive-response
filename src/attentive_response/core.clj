(ns attentive-response.core
  (:require [clojure.string :as string]
            [com.twinql.clojure.conneg :as conneg]
            [cognitect.transit :as transit]
            [cheshire.core :as json])
  (:import [java.io ByteArrayOutputStream]))

(defmulti encode-response-type
  (fn [request _]
    (-> (get-in request [:headers "accept"])
        conneg/best-allowed-content-type
        vec)))

(defmethod encode-response-type :default [_ response]
  (merge response
    {:headers {"Content-Type" "application/json; charset=utf-8"}
     :body (json/generate-string (:body response))}))

(defmethod encode-response-type ["application" "edn"] [_ response]
  (merge response
    {:headers {"Content-Type" "application/edn; charset=utf-8"}
     :body (pr-str (:body response))}))

(defn- write-transit [x t opts]
  (let [baos (ByteArrayOutputStream.)
        w    (transit/writer baos t opts)
        _    (transit/write w x)
        ret  (.toString baos)]
    (.reset baos)
    ret))

(defmethod encode-response-type ["application" "transit+json"] [_ response]
  (merge response
    {:headers {"Content-Type" "application/transit+json; charset=utf-8"}
     :body (write-transit (:body response) :json {})}))

(defn wrap-attentive-response [handler]
  (fn [request]
    (let [response (handler request)]
      (if (coll? (:body response))
        (encode-response-type request response)
        response))))
