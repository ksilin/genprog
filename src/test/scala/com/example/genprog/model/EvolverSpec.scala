package com.example.genprog.model

import org.scalatest.{ FreeSpec, MustMatchers }

import scala.collection.immutable

class EvolverSpec extends FreeSpec with MustMatchers with FunctionsAndTerminals {

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

  "must mutate tree" in {
    val mutated = Evolver.mutate(functionSet, terminalSet, 5, exp)
    println(exp)
    println(mutated)
  }

  "must select the fittest in tournament" in {

    val candidates = List((Con(3), 3f), (Con(1), 1f), (Con(4), 4f), (Con(2), 2f))
    Evolver.tournament(candidates) mustBe Con(1)
  }

  "must perform crossover between individuals" in {

    val mom = Sub(Var('z), Add(Con(1), Con(2)))
    val dad = Sub(Var('x), Var('y))

    val child = Evolver.crossover(mom, dad)
    println(child)
  }

  "must perform crossovers on population" in {

    val pop: Seq[Exp] = (1 to 10) map (_ => AstBuilder.grow(5, functionSet, terminalSet))

    val targetFunction: Float => Float = x => (Math.pow(x, 2) - x - 2).toFloat

    // orignal data to approximate
    val cases: Map[ST, Float] = (-1f)
      .to(1f, 0.05f)
      .map(x => (Map('x -> x), targetFunction(x)))
      .toMap

    val fitness = Fitness.fitness(cases)

    val withFitness = pop map (exp => {
      (exp, fitness(exp))
    })

    val expected = withFitness sortBy (_._2) take (withFitness.size * 0.7f).toInt
    expected foreach println

    val result: Set[Exp] = Evolver.crossovers(withFitness, 0.7f)
    println("evolved: ")
    result foreach println
  }

  "must perform mutations on population" in {
    val pop: Seq[Exp] = (1 to 10) map (_ => AstBuilder.grow(5, functionSet, terminalSet))
    pop foreach println
    val result: Set[Exp] = Evolver.mutants(functionSet, terminalSet, 5, pop, 0.3f)
    println("mutated: ")
    result foreach println
  }

}
