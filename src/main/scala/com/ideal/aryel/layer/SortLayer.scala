package com.ideal.aryel.layer

import com.ideal.aryel.datasource.FieldElement


class SortLayer(attribute: String, ascending: Boolean = true) {
  def asc = new SortLayer(attribute, true)
  def desc = new SortLayer(attribute, false)
  def compare[T <: FieldElement](a: T, b: T): Int = {
    val first = a.access(attribute)
    val second = b.access(attribute)
    (first, second) match {
      case (null, null) => 0
      case (null, _) => -1
      case (_, null) => 1
      case _ => {
        val result = first.asInstanceOf[Comparable[Any]].compareTo(second)
        if (ascending) {
          result
        } else {
          result * -1
        }
      }
    }
  }
}
