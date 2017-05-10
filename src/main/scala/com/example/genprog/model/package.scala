package com.example.genprog

import scala.collection.immutable
import scala.util.Random

package object model {

  type ST = Map[Symbol, Float]

  def random(): Float = Random.nextFloat()

  def randomElement[T](elements: IndexedSeq[T]): T =
    elements(Random.nextInt(elements.length))

}
