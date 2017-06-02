/*
 * Copyright (C) 2009-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.brightit.sird

class RequiredQueryStringParameter(paramName: String) extends QueryStringParameterExtractor[String] {
  def unapply[T](t: T)(implicit sc: IsSirdQueryStringCompatible[T]): Option[String] =
    sc.queryParam(t, paramName).flatMap(_.headOption)

  //  def unapply(qs: QueryString): Option[String] = qs.get(paramName).flatMap(_.headOption)
}

class OptionalQueryStringParameter(paramName: String) extends QueryStringParameterExtractor[Option[String]] {
  def unapply[T](t: T)(implicit sc: IsSirdQueryStringCompatible[T]): Option[Option[String]] =
    Some(sc.queryParam(t, paramName).flatMap(_.headOption))

//  def unapply(qs: QueryString): Option[Option[String]] = Some(qs.get(paramName).flatMap(_.headOption))
}

class SeqQueryStringParameter(paramName: String) extends QueryStringParameterExtractor[Seq[String]] {
  def unapply[T](t: T)(implicit sc: IsSirdQueryStringCompatible[T]): Option[Seq[String]] =
    Some(sc.queryParam(t, paramName).fold(Seq.empty[String])(identity))
}

trait QueryStringParameterExtractor[A] {
  def unapply[T](t: T)(implicit sc: IsSirdQueryStringCompatible[T]): Option[A]
}

object QueryStringParameterExtractor {

  def required(name: String) = new RequiredQueryStringParameter(name)
  def optional(name: String) = new OptionalQueryStringParameter(name)
  def seq(name: String) = new SeqQueryStringParameter(name)
}

