package com.ideal.aryel.layer

import com.ideal.aryel.datasource.FieldElement

class PredicateLayer[T <: FieldElement](attribute: String, predicate: Comparable[Any] => Boolean) {
  def test(elem: T): Boolean = predicate(elem.access(attribute).asInstanceOf[Comparable[Any]])
}
