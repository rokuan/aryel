package com.ideal.aryel.dsl

import com.ideal.aryel.datasource.FieldElement
import com.ideal.aryel.layer.{LimitLayer, PredicateLayer, SortLayer}

object AryelDSL {
  implicit def dataSourceToSeq[T <: FieldElement](dataSource: DataSource[T]) = dataSource.source

  implicit class DataSource[T <: FieldElement](val source: Seq[T]) {
    def |>(layer: SortLayer): DataSource[T] = source.sorted[T](layer.compare)
    def |>(layer: PredicateLayer): DataSource[T] = source.filter(layer.test)
    def |>(layer: LimitLayer): DataSource[T] = source.take(layer.limit)
  }

  implicit class LayerAttribute[T <: FieldElement](attribute: String) {
    protected final def comparePredicate(value: Comparable[_], predicate: Int => Boolean) = new PredicateLayer(attribute, c => predicate(c.compareTo(value)))
    def _lt(value: Comparable[_]): PredicateLayer = comparePredicate(value, _ < 0)
    def _gt(value: Comparable[_]): PredicateLayer = comparePredicate(value, _ > 0)
    def _lte(value: Comparable[_]): PredicateLayer = comparePredicate(value, _ <= 0)
    def _gte(value: Comparable[_]): PredicateLayer = comparePredicate(value, _ >= 0)
    def _eq(value: Comparable[_]): PredicateLayer = comparePredicate(value, _ == 0)
    def _neq(value: Comparable[_]): PredicateLayer = comparePredicate(value, _ != 0)
    def _between[N](bound: Bound[N]): PredicateLayer = new PredicateLayer(attribute, bound.contains(_))
    def _contains(value: String): PredicateLayer = new PredicateLayer(attribute, _.toString.toLowerCase.contains(value.toLowerCase))
  }

  abstract class BetweenRange[T <: Comparable[T]](lowerBound: T) {
    def and(upperBound: Comparable[T]) = new Bound[T](lowerBound, upperBound)
  }

  implicit class DoubleRange(d: Double) extends BetweenRange[java.lang.Double](d)
  implicit class LongRange(l: Long) extends BetweenRange[java.lang.Long](l)
  implicit class IntRange(i: Int) extends BetweenRange[java.lang.Integer](i)
  implicit class StringRange(s: String) extends BetweenRange[java.lang.String](s)

  def limit(size: Int) = new LimitLayer(size)
  def orderBy(attribute: String) = new SortLayer(attribute)

  case class Bound[T](from: Comparable[T], to: Comparable[T]) {
    def contains(value: Comparable[Any]): Boolean = value.compareTo(from) > 0 && value.compareTo(to) < 0
  }
}
