package com.example.genprog.model

import org.scalatest.{ FreeSpec, MustMatchers }

class AstBuilderSpec extends FreeSpec with MustMatchers with FunctionsAndTerminals {

  "must generate uniform tree" in {
    val ast = AstBuilder.full(3, functionSet, terminalSet)
    println(ast)

    val res = Exp.eval(ast, Map('x -> 2))
    println(res)
  }

  "must generate non-uniform tree" in {
    val ast = AstBuilder.grow(3, functionSet, terminalSet)
    println(ast)

    val res = Exp.eval(ast, Map('x -> 2))
    println(res)
  }

  "must generate initial population (ramp)" in {
    val population = AstBuilder.ramp(100, 5, functionSet, terminalSet)
    population foreach println
  }

}
