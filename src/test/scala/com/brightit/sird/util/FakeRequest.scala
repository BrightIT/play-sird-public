package com.brightit.sird.util

import com.brightit.sird.RequestData

object FakeRequest {
  /**
   * Create request data to test routing.
   *
   * Naming mimics the Play `FakeRequest`.
   * @param pseudoUri this param isn't parsed like a proper URI, only enough to allow writing tests.
   */
  def fakeRequest(method: String, pseudoUri: String): RequestData = pseudoUri.split('?') match {
    case Array(path) => RequestData(method, path, Map.empty)
    case Array(path, queryStr) =>
      val query =
        queryStr.split('&')
          .map { str => val Array(k, v) = str.split("="); k -> v }
          .toList
          .groupBy(_._1)
          .mapValues(_ map (_._2))
      RequestData(method, path, query)
  }
}
