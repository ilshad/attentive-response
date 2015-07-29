(ns attentive-response.core
  (:require [clojure.string :as string]
            [com.twinql.clojure.conneg :as conneg]
            [cognitect.transit :as transit]
            [cheshire.core :as json])
  (:import [java.io ByteArrayOutputStream]))

(defn- encode-respone-type* [response content-type body]
  (-> (assoc-in response [:headers "Content-Type"] content-type)
      (assoc :body body)))

(defn- write-transit [x t opts]
  (let [baos (ByteArrayOutputStream.)
        w    (transit/writer baos t opts)
        _    (transit/write w x)
        ret  (.toString baos)]
    (.reset baos)
    ret))

(defmulti encode-response-type
  (fn [request _]
    (-> (get-in request [:headers "accept"])
        conneg/best-allowed-content-type
        vec)))

(defmethod encode-response-type :default [_ response]
  (encode-respone-type* response "application/json; charset=utf-8"
    (json/generate-string (:body response))))

(defmethod encode-response-type ["application" "edn"] [_ response]
  (encode-respone-type* response "application/edn; charset=utf-8"
    (pr-str (:body response))))

(defmethod encode-response-type ["application" "transit+json"] [_ response]
  (encode-respone-type* response "application/transit+json; charset=utf-8"
    (write-transit (:body response) :json {})))

(defn wrap-attentive-response [handler]
  (fn [request]
    (let [response (handler request)]
      (if (coll? (:body response))
        (encode-response-type request response)
        response))))
