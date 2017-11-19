package com.ideal.aryel.datasource

trait FieldElement {
  def access(attribute: String): Comparable[_]
}
