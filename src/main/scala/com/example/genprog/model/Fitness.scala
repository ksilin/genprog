package com.example.genprog.model

object Fitness {

  def fitness(cases: Map[ST, Float])(tree: Exp): Float =
    (cases map {
      case (symbols, expected) =>
        val actual = Exp.eval(tree, symbols)
        Math.abs(expected - actual)
    }).sum // aka reduce (_ + _)

}
