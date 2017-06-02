package com.brightit.sird

import java.net.{URI, URL}

trait IsSirdMethodCompatible[T] {
  def method(t: T): String
}

trait IsSirdPathCompatible[T] {
  def path(t: T): String
}

trait IsSirdQueryStringCompatible[T] {
  def queryParam(t: T, s: String): Option[Seq[String]]
}

trait IsSirdCompatible[T]
  extends IsSirdMethodCompatible[T]
  with IsSirdPathCompatible[T]
  with IsSirdQueryStringCompatible[T]

object IsSirdPathCompatible {
  implicit val StringIsSirdPathCompatible: IsSirdPathCompatible[String] =
    new IsSirdPathCompatible[String] {
      override def path(s: String) = s
    }

  implicit val UriIsSirdPathCompatible: IsSirdPathCompatible[URI] =
    new IsSirdPathCompatible[URI] {
      override def path(uri: URI) = uri.getPath match {
        case null => ""
        case path => path
      }
    }

  implicit val UrlIsSirdPathCompatible: IsSirdPathCompatible[URL] =
    new IsSirdPathCompatible[URL] {
      override def path(url: URL) = url.getPath match {
        case null => ""
        case path => path
      }
    }
}

object IsSirdQueryStringCompatible {
  private def parse(query: String): QueryString = {
    Option(query).map(_.split("&").toSeq.map { keyValue =>
      keyValue.split("=", 2) match {
        case Array(key, value) => key -> value
        case Array(key) => key -> ""
      }
    }.groupBy(_._1).mapValues(_.map(_._2)).toMap)
      .getOrElse(Map.empty)
  }

  implicit val UriIsSirdQueryStringCompatible: IsSirdQueryStringCompatible[URI] =
    new IsSirdQueryStringCompatible[URI] {
      override def queryParam(uri: URI, s: String) = parse(uri.getQuery).get(s)
    }

  implicit val UrlIsSirdQueryStringCompatible: IsSirdQueryStringCompatible[URL] =
    new IsSirdQueryStringCompatible[URL] {
      override def queryParam(url: URL, s: String) = parse(url.getQuery).get(s)
    }

  implicit val QueryStringMapIsSirdQueryStringCompatible: IsSirdQueryStringCompatible[QueryString] =
    new IsSirdQueryStringCompatible[QueryString] {
      override def queryParam(queryString: QueryString, s: String) = queryString.get(s)
    }
}
