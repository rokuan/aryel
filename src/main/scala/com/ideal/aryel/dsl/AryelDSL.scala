package com.ideal.aryel.dsl

import com.ideal.aryel.datasource.FieldElement
import com.ideal.aryel.layer.PredicateLayer

object AryelDSL {
  implicit def dataSourceToSeq[T <: FieldElement](dataSource: DataSource[T]) = dataSource.source

  implicit class DataSource[T <: FieldElement](val source: Seq[T]) {
    def |>(layer: PredicateLayer[T]): DataSource[T] = source.filter(layer.test)
  }

  implicit class LayerAttribute[T <: FieldElement](attribute: String) {
    protected final def comparePredicate(value: Comparable[_], predicate: Int => Boolean) = new PredicateLayer[T](attribute, c => predicate(c.compareTo(value)))
    def _lt(value: Comparable[_]): PredicateLayer[T] = comparePredicate(value, _ < 0)
    def _gt(value: Comparable[_]): PredicateLayer[T] = comparePredicate(value, _ > 0)
    def _lte(value: Comparable[_]): PredicateLayer[T] = comparePredicate(value, _ <= 0)
    def _gte(value: Comparable[_]): PredicateLayer[T] = comparePredicate(value, _ >= 0)
    def _eq(value: Comparable[_]): PredicateLayer[T] = comparePredicate(value, _ == 0)
    def _between[N](bound: Bound[N]): PredicateLayer[T] = new PredicateLayer[T](attribute, bound.contains(_))
  }

  abstract class BetweenRange[T <: Comparable[T]](lowerBound: T) {
    def and(upperBound: Comparable[T]) = new Bound[T](lowerBound, upperBound)
  }

  implicit class DoubleRange(d: Double) extends BetweenRange[java.lang.Double](d)
  implicit class LongRange(l: Long) extends BetweenRange[java.lang.Long](l)
  implicit class IntRange(i: Int) extends BetweenRange[java.lang.Integer](i)
  implicit class StringRange(s: String) extends BetweenRange[java.lang.String](s)

  case class Bound[T](from: Comparable[T], to: Comparable[T]) {
    def contains(value: Comparable[Any]): Boolean = value.compareTo(from) > 0 && value.compareTo(to) < 0
  }
}
