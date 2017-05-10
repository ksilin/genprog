package com.example.genprog.model

import org.scalatest.{ FreeSpec, MustMatchers }

class FitnessSpec extends FreeSpec with MustMatchers {

  // leafs of the AST are referred to as the terminal set, the branches are the function set
  val terminalSet = IndexedSeq(Var('x)) ++ 1f.to(5f, 1f).map(Con)
  val functionSet = IndexedSeq(Add, Sub, Div, Mul)

  val cases = (-1f)
    .to(1f, 0.05f)
    .map(x => (Map('x -> x), Math.pow(x, 2) - x - 2))
    .toMap mapValues (_.toFloat)

  val fitness = Fitness.fitness(cases)

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
