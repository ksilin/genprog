package com.example.genprog

import com.example.genprog.model.{ AstBuilder, Evolver, Exp, ST }

object Main {

  def run(functionSet: Seq[(Exp, Exp) => Exp],
          terminalSet: Seq[Exp],
          cases: Map[ST, Float],
          fitness: Map[ST, Float] => Exp => Float,
          criteria: Float => Boolean,
          maxRuns: Int = 1000,
          maxDepth: Int = 6,
          populationSize: Int = 100000): Exp = {

    val firstGen: Seq[Exp] =
      AstBuilder.ramp(populationSize, maxDepth, functionSet, terminalSet).toSeq

    val fitFunc = fitness(cases)

    def loop(run: Int, currentPopulation: Seq[Exp]): Exp = {

      val withFitness           = currentPopulation map (exp => (exp, fitFunc(exp)))
      val (topTree, minFitness) = fittest(withFitness)
      val mutate: (Seq[Exp], Float, Set[Exp]) => Set[Exp] =
        Evolver.mutants(functionSet, terminalSet, maxDepth, _: Seq[Exp], _: Float, _: Set[Exp])
      println(s"iteration: ${run}, minFit: ${minFitness}")
      if (criteria(minFitness))
        topTree
      else {

        val fittest: Set[Exp] = Evolver.replicas(withFitness)
        val mutants: Set[Exp] = mutate(currentPopulation, 0.01f, fittest)
        val evolved: Set[Exp] = Evolver.crossovers(withFitness, 0.8f, mutants)
        loop(run + 1, evolved.toSeq)
      }
    }
    loop(1, firstGen)
  }

  def fittest(treesAndFitness: Seq[(Exp, Float)]): (Exp, Float) =
    treesAndFitness.minBy { case (_, fit) => fit }

}
