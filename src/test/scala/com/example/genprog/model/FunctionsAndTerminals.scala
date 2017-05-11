package com.example.genprog.model

trait FunctionsAndTerminals {

  // leafs of the AST are referred to as the terminal set, the branches are the function set
  val terminalSet = Seq(Var('x)) ++ 1f.to(5f, 1f).map(Con)
  val functionSet = Seq(Add, Sub, Div, Mul)

}
