package com.example.genprog

import com.example.genprog.model.{ Exp, Fitness, FunctionsAndTerminals, ST }
import org.scalatest.{ FreeSpec, MustMatchers }

class MainSpec extends FreeSpec with MustMatchers with FunctionsAndTerminals {

  "must run" in {

    val maxDepth                       = 6
    val targetFunction: Float => Float = x => (Math.pow(x, 3) + Math.pow(x, 2) - x - 2).toFloat

    // orignal data to approximate
    val cases: Map[ST, Float] = (-3f)
      .to(3f, 0.1f)
      .map(x => (Map('x -> x), targetFunction(x)))
      .toMap

    val criteria: Float => Boolean = _ < 30f

    val fitTree: Exp =
      Main.run(functionSet, terminalSet, cases, Fitness.fitness, criteria, populationSize = 1000)
    println(fitTree)

    val res: Iterable[(Float, Float)] = cases map {
      case (symbols, expected) =>
        (symbols('x), Exp.eval(fitTree, symbols))
    }

    val expectedData = cases map { case (symbols, expected) => (symbols('x), expected) }

    val dataList = List("expected" -> expectedData, "actual" -> res)
  }

}
