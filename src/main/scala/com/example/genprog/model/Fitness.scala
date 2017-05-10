package com.example.genprog.model

object Fitness {

  val fitness: (Map[ST, Float]) => (Exp) => Float = cases =>
    tree =>
      (cases map {
        case (symbols, expected) =>
          val actual = Exp.eval(tree, symbols)
          Math.abs(expected - actual)
      }).sum // aka reduce (_ + _)

}
