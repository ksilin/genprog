package com.example.genprog.model

import com.example.genprog.model

import scala.annotation.tailrec

object Evolver {

  def mutate(functionSet: Seq[(Exp, Exp) => Exp],
             terminalSet: Seq[Exp],
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
    val subtrees: Seq[Exp] = collectAll(tree)
    randomElement(subtrees)
  }

  def collectAll(tree: Exp): Seq[Exp] = collect(tree) { case e => e }

  def collect[T](tree: Exp)(pf: PartialFunction[Exp, T]): Seq[T] = {
    def loop(subtree: Exp, acc: Seq[T]): Seq[T] = {
      val result = if (pf.isDefinedAt(subtree)) acc :+ pf(subtree) else acc
      subtree match {
        case v: Var   => result
        case c: Con   => result
        case o: BinOp => result ++ loop(o.lhs, acc) ++ loop(o.rhs, acc)
      }
    }
    loop(tree, Seq.empty)
  }

  def tournament(exps: Seq[(Exp, Float)]): Exp =
    exps.minBy { case (exp, fitness) => fitness }._1

  def crossover(left: Exp, right: Exp): Exp = {
    val replacement = randomElement[Exp](biasedCollect(left))
    val target      = randomElement[Exp](biasedCollect(right))
    replace(right, target, replacement)
  }

  // prefers ops to terminals
  def biasedCollect(tree: Exp): Seq[Exp] = {
    val ops = collectOps(tree)
    if (model.random() > 0.9 || ops.isEmpty)
      collectTerminals(tree)
    else ops
  }

  def collectOps(tree: Exp): Seq[Exp] = collect(tree) { case o: BinOp => o }

  def collectTerminals(tree: Exp): Seq[Exp] = collect(tree) {
    case v: Var => v
    case c: Con => c
  }

  // original uses recursive set filling. I dont see the point
  def replicas(populationAndFitness: Seq[(Exp, Float)], survivalRatio: Float = 0.19f): Set[Exp] =
    (populationAndFitness sortBy (_._2) map (_._1) take (populationAndFitness.size * survivalRatio).toInt).toSet

  def crossovers(populationAndFitness: Seq[(Exp, Float)],
                 survivalRatio: Float = .8f,
                 acc: Set[Exp] = Set.empty): Set[Exp] = {

    val length = acc.size + (populationAndFitness.size.toFloat * survivalRatio)

    @tailrec
    def loop(acc: Set[Exp]): Set[Exp] =
      if (acc.size == length) { acc } else {
        loop(
          acc + crossover(
            tournament(
              List(randomElement(populationAndFitness), randomElement(populationAndFitness))
            ),
            tournament(
              List(randomElement(populationAndFitness), randomElement(populationAndFitness))
            )
          )
        )
      }
    loop(acc)
  }

  def mutants(functionSet: Seq[(Exp, Exp) => Exp],
              terminalSet: Seq[Exp],
              maxDepth: Int,
              population: Seq[Exp],
              survivalRatio: Float = 0.01f,
              acc: Set[Exp] = Set.empty): Set[Exp] = {

    def mut    = mutate(functionSet, terminalSet, maxDepth, _: Exp)
    val length = acc.size + (population.size.toFloat * survivalRatio)

    @tailrec
    def loop(acc: Set[Exp]): Set[Exp] =
      if (acc.size == length) acc
      else loop(acc + mut(randomElement(population)))
    loop(acc)
  }

}
