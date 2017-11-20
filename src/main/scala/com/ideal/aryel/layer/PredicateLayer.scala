package com.ideal.aryel.layer

import com.ideal.aryel.datasource.FieldElement

class PredicateLayer(attribute: String, predicate: Comparable[Any] => Boolean) {
  def test[T <: FieldElement](elem: T): Boolean = predicate(elem.access(attribute).asInstanceOf[Comparable[Any]])
}
