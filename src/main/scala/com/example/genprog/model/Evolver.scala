package com.example.genprog.model

object Evolver {

  def mutate(functionSet: IndexedSeq[(Exp, Exp) => Exp],
             terminalSet: IndexedSeq[Exp],
             maxDepth: Int,
             exp: Exp): Exp = {
    val target      = AstBuilder.random(exp)
    val replacement = AstBuilder.grow(maxDepth, functionSet, terminalSet)
    replace(exp, target, replacement)
  }

  def replace(exp: Exp, target: Exp, replacement: Exp): Exp = {
    // just an 'alias' for simplicity
    def repl(exp: Exp) = replace(exp, target, replacement)

    exp match {
      case exp: Exp if exp.eq(target) => replacement
      case Add(lhs, rhs)              => Add(repl(lhs), repl(rhs))
      case Sub(lhs, rhs)              => Sub(repl(lhs), repl(rhs))
      case Mul(lhs, rhs)              => Mul(repl(lhs), repl(rhs))
      case Div(lhs, rhs)              => Div(repl(lhs), repl(rhs))
      case x                          => x
    }
  }

}
