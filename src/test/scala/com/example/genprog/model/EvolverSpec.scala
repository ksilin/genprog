package com.example.genprog.model

import org.scalatest.{ FreeSpec, MustMatchers }

class EvolverSpec extends FreeSpec with MustMatchers {

  // x² - x - 2
  val exp = Sub(Sub(Mul(Var('x), Var('x)), Var('x)), Con(2))

  "must replace subtree with given exp by reference" in {
    val unchanged = Evolver.replace(exp, Var('x), Con(3))
    // no changes because reference equality is expected fo replace
    unchanged mustBe exp

    val replaced = Evolver.replace(exp, exp.lhs, Con(3))
    val expected = Sub(Con(3), Con(2))
    replaced mustBe expected
  }

  "must decompose tree in all teh subtrees" in {
    val subtrees = Evolver.collectAll(exp)

    val expected = List(Sub(Sub(Mul(Var('x), Var('x)), Var('x)), Con(2)),
                        Sub(Mul(Var('x), Var('x)), Var('x)),
                        Mul(Var('x), Var('x)),
                        Var('x),
                        Var('x),
                        Var('x),
                        Con(2))

    subtrees must contain allElementsOf expected
    expected must contain allElementsOf subtrees
  }

}
