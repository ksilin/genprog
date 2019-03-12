package com.example.genprog.helisa

import org.scalatest.{ FreeSpec, MustMatchers }

case class Guess(num: Int)

class HelisaInitSpec extends FreeSpec with MustMatchers {

  import com.softwaremill.helisa._

  val genotype =
    () => genotypes.uniform(chromosomes.int(0, 100))

  def fitness(toGuess: Int) =
    (guess: Guess) => 1.0 / (guess.num - toGuess).abs

  val Number = 72

  val evolver =
    Evolver(fitness(Number), genotype)
      .populationSize(10)
      .phenotypeValidator(_.num % 2 == 0)
      .build()

  "must run evolver" in {
    val run = evolver.iterator() // <1>

    val resultAfter2 = run
      .drop(2)
      .next()

    println(resultAfter2.bestPhenotype)

    val resultAfter100 = run
      .drop(97)
      .next()

    println(resultAfter100.bestPhenotype)
  }

}
