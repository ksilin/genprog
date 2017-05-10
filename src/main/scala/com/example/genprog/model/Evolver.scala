package com.example.genprog.model

import scala.collection.immutable

object Evolver {

  def mutate(functionSet: IndexedSeq[(Exp, Exp) => Exp],
             terminalSet: IndexedSeq[Exp],
             maxDepth: Int,
             exp: Exp): Exp = {
    val target      = random(exp)
    val replacement = AstBuilder.grow(maxDepth, functionSet, terminalSet)
    replace(exp, target, replacement)
  }

  def replace(exp: Exp, target: Exp, replacement: Exp): Exp = {
    // just an 'alias' for simplicity
    def repl(exp: Exp) = replace(exp, target, replacement)

    exp match {
      // using reference equality so we replace at most one subtree
      case exp: Exp if exp.eq(target) => replacement
      case Add(lhs, rhs)              => Add(repl(lhs), repl(rhs))
      case Sub(lhs, rhs)              => Sub(repl(lhs), repl(rhs))
      case Mul(lhs, rhs)              => Mul(repl(lhs), repl(rhs))
      case Div(lhs, rhs)              => Div(repl(lhs), repl(rhs))
      case x                          => x
    }
  }

  def random(tree: Exp): Exp = {
    val subtrees: IndexedSeq[Exp] = collectAll(tree)
    randomElement(subtrees)
  }

  def collectAll(tree: Exp): IndexedSeq[Exp] = collect(tree) { case e => e }

  def collect[T](tree: Exp)(pf: PartialFunction[Exp, T]): IndexedSeq[T] = {
    def loop(subtree: Exp, acc: IndexedSeq[T]): IndexedSeq[T] = {
      val result = if (pf.isDefinedAt(subtree)) acc :+ pf(subtree) else acc
      subtree match {
        case v: Var   => result
        case c: Con   => result
        case o: BinOp => result ++ loop(o.lhs, acc) ++ loop(o.rhs, acc)
      }
    }
    loop(tree, IndexedSeq.empty)
  }

}
