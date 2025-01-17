; Copyright 2023 Ingy dot Net
; This code is licensed under MIT license (See License for details)

(ns yamlscript.resolver-test
  (:use yamlscript.debug)
  (:require
   [clojure.edn :as edn]
   [yamlscript.parser :as parser]
   [yamlscript.composer :as composer]
   [yamlscript.resolver :as resolver]
   [yamltest.core :as test]))

(test/load-yaml-test-files
  ["test/compiler-stack.yaml"
   "test/data-mode.yaml"
   "test/resolver.yaml"
   "test/compiler.yaml"]
  {:pick-func #(test/has-keys? [:yamlscript :resolve] %)
   :test-func (fn [test]
                (try
                  (->> test
                    :yamlscript
                    parser/parse
                    composer/compose
                    resolver/resolve)
                  (catch Exception e
                    (if (:error test)
                      (.getMessage e)
                      (throw e)))))
   :want-func (fn [test]
                (->> test
                  :resolve
                  edn/read-string))})
