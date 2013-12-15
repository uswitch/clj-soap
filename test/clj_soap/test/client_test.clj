(ns clj-soap.test.client-test
  (:use clojure.test)
  (:require [clj-soap.core :as c]))

(deftest should-not-configure-proxy-when-hostname-not-provided
  (let [client-options (c/make-client-options "www.foo.com" {})]
    (is (nil? (-> client-options (.getProperty org.apache.axis2.transport.http.HTTPConstants/PROXY))))
    ))

(deftest should-configure-proxy-properties-when-provided
  (let [o {:proxy-host "www.bar.com" :proxy-port 1234}
        client-options (c/make-client-options "www.foo.com" o)
        proxy-props (-> client-options (.getProperty org.apache.axis2.transport.http.HTTPConstants/PROXY))]
      (is (= (:proxy-host o) (-> proxy-props .getProxyHostName)))
      (is (= (:proxy-port o) (-> proxy-props .getProxyPort)))
    ))

(deftest should-default-to-3128-when-proxy-port-not-provided  
  (let [o {:proxy-host "www.bar.com"}
        client-options (c/make-client-options "www.foo.com" o)
        proxy-props (-> client-options (.getProperty org.apache.axis2.transport.http.HTTPConstants/PROXY))]
      (is (= (:proxy-host o) (-> proxy-props .getProxyHostName)))
      (is (= 3128 (-> proxy-props .getProxyPort)))
    ))

(deftest should-have-default-timeouts
  (let [client-options (c/make-client-options "www.foo.com" {})]
      (is (= org.apache.axis2.client.Options/DEFAULT_TIMEOUT_MILLISECONDS (-> client-options .getTimeOutInMilliSeconds)))
      (is (nil? (-> client-options (.getProperty org.apache.axis2.transport.http.HTTPConstants/SO_TIMEOUT))))
      (is (nil? (-> client-options (.getProperty org.apache.axis2.transport.http.HTTPConstants/CONNECTION_TIMEOUT))))
    ))

(deftest should-configure-timeouts-when-provided
  (let [o {:socket-timeout 1000 :conn-timeout 2000}
        client-options (c/make-client-options "www.foo.com" o)]
      (is (= (:socket-timeout o) (-> client-options .getTimeOutInMilliSeconds)))
      (is (= (:socket-timeout o) (-> client-options (.getProperty org.apache.axis2.transport.http.HTTPConstants/SO_TIMEOUT))))
      (is (= (:conn-timeout o) (-> client-options (.getProperty org.apache.axis2.transport.http.HTTPConstants/CONNECTION_TIMEOUT))))
    ))
