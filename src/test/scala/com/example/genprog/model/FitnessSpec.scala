package com.example.genprog.model

import org.scalatest.{ FreeSpec, MustMatchers }

class FitnessSpec extends FreeSpec with MustMatchers with FunctionsAndTerminals {

  val targetFunction: Float => Float = x => (Math.pow(x, 2) - x - 2).toFloat

  // orignal data to approximate
  val cases: Map[ST, Float] = (-1f)
    .to(1f, 0.05f)
    .map(x => (Map('x -> x), targetFunction(x)))
    .toMap

  val fitness: (Exp) => Float = Fitness.fitness(cases)

  "fitness of random ast" in {
    val tree = AstBuilder.grow(5, functionSet, terminalSet)
    val fl   = fitness(tree)
    (fl > 0) mustBe true
  }

  "perfect match -> optimal fitness" in {
    val exp = Sub(Sub(Mul(Var('x), Var('x)), Var('x)), Con(2))
    val fl  = fitness(exp)
    fl mustBe 0
  }

}
