(def +version+ "0.1.1-SNAPSHOT")

(task-options!
  pom {:project 'ilshad/attentive-response
       :version +version+
       :description "Content negotiation and encoding"
       :url "https://github.com/ilshad/attentive-response"
       :scm {:url "https://github.com/ilshad/attentive-response"}
       :license {"Eclipse Public License"
                 "http://www.eclipse.org/legal/epl-v10.html"}})

(set-env!
  :source-paths #{"src"}
  :resource-paths #{"src"}
  :dependencies '[[org.clojure/clojure "1.7.0" :scope "provided"]
                  [org.clojure/tools.logging "0.3.1" :scope "provided"]
                  [adzerk/bootlaces "0.1.11" :scope "test"]
                  [adzerk/boot-test "1.0.4" :scope "test"]
                  [com.twinql.clojure/clj-conneg "1.1.0"]
                  [com.cognitect/transit-clj "0.8.275"]
                  [cheshire "5.5.0"]])

(require '[adzerk.bootlaces :refer :all])
(require '[adzerk.boot-test :refer :all])

(defn test-env! []
  (merge-env! :source-paths #{"test"}))

(deftask run-tests []
  (test-env!)
  (test))

(bootlaces! +version+)
