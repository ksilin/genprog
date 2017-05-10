package com.example.genprog.model

import scala.util.Random

object AstBuilder {

  // generates uniform full asts
  def full(depth: Int, functions: IndexedSeq[(Exp, Exp) => Exp], terminals: IndexedSeq[Exp]): Exp = {
    def loop(i: Int): Exp =
      if (i == depth)
        random(terminals)
      else random(functions)(loop(i + 1), loop(i + 1))

    loop(0)
  }

  def grow(depth: Int, functions: IndexedSeq[(Exp, Exp) => Exp], terminals: IndexedSeq[Exp]): Exp = {

    def randomStop: Boolean = {
      val tl = terminals.size.toFloat
      val fl = functions.size.toFloat
      random() < tl / (tl + fl)
    }

    def loop(i: Int): Exp =
      if (i == depth || randomStop)
        random(terminals)
      else
        random(functions)(loop(i + 1), loop(i + 1))

    loop(0)
  }

  def ramp(count: Int,
           maxDepth: Int,
           functions: IndexedSeq[(Exp, Exp) => Exp],
           terminals: IndexedSeq[Exp]): Set[Exp] = {

    def loop(acc: Set[Exp], i: Int, depth: Int): Set[Exp] =
      if (i == count) {
        acc
      } else {
        val tree = if (i % 2 == 0) {
          full(depth, functions, terminals)
        } else {
          grow(depth, functions, terminals)
        }
        val nextDepth = if (depth == maxDepth) 1 else depth + 1
        if (acc.contains(tree)) {
          loop(acc, i, nextDepth)
        } else {
          loop(acc + tree, i + 1, nextDepth)
        }
      }
    loop(Set.empty, 0, 1)
  }

  def random(): Float = Random.nextFloat()

  def random[T](elements: IndexedSeq[T]): T =
    elements(Random.nextInt(elements.length))

  def random(tree: Exp): Exp = random(collectAll(tree))

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
