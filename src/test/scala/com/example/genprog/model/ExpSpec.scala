package com.example.genprog.model

import org.scalatest.{ FreeSpec, MustMatchers }

class ExpSpec extends FreeSpec with MustMatchers {

  "must eval 2nd degree poly" in {

    // x² - x - 2
    val exp = Sub(Sub(Mul(Var('x), Var('x)), Var('x)), Con(2))

    val res = Exp.eval(exp, Map('x -> -3))
    println(res)
  }

  "must eval 3rd degree poly" in {
//    x^3 / 4 + 3x^2 / 4 - 3x / 2 - 2
    val exp = Sub(Sub(Add(Div(Mul(Var('x), Mul(Var('x), Var('x))), Con(4)),
                          Div(Mul(Con(3), Mul(Var('x), Var('x))), Con(4))),
                      Div(Mul(Con(3), Var('x)), Con(2))),
                  Con(2))

    val res = Exp.eval(exp, Map('x -> 0))
    res mustBe -2
  }

}
