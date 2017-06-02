/*
 * Copyright (C) 2009-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package com.brightit.sird

import scala.util.control.NonFatal

/**
 * An extractor that extracts from a String.
 *
 * Originally this was `PathBindableExtractor`, but there's no need to base this specifically on Play's `PathBindable`.
 */
abstract class StringExtractor[T] {
  self =>

  /**
   * Extract s to T if it can be bound, otherwise don't match.
   */
  def unapply(s: String): Option[T]

  /**
   * Extract Option[T] only if s is None, Some value that can be bound, otherwise don't match.
   */
  def unapply(s: Option[String]): Option[Option[T]] = {
    s match {
      case None => Some(None)
      case Some(self(value)) => Some(Some(value))
      case _ => None
    }
  }

  /**
   * Extract Seq[T] only if ever element of s can be bound, otherwise don't match.
   */
  def unapply(s: Seq[String]): Option[Seq[T]] = {
    val bound = s.collect {
      case self(value) => value
    }
    if (bound.length == s.length) {
      Some(bound)
    } else {
      None
    }
  }
}

object StringExtractor {
  /**
   * Create a [[StringExtractor]] based on a function which can throw.
   */
  def pseudoparser[T](f: String => T): StringExtractor[T] = new StringExtractor[T] {
    override def unapply(s: String) = try { Some(f(s)) } catch { case NonFatal(_) => None }
  }
}

/**
 * Extractors that bind types from strings.
 */
trait StringExtractors {
  import StringExtractor._

  /**
   * An int extractor.
   */
  val int = pseudoparser(_.toInt)

  /**
   * A long extractor.
   */
  val long = pseudoparser(_.toLong)

  /**
   * A boolean extractor.
   */
  val bool = pseudoparser(_.toBoolean)

  /**
   * A float extractor.
   */
  val float = pseudoparser(_.toFloat)

  /**
   * A double extractor.
   */
  val double = pseudoparser(_.toDouble)
}
