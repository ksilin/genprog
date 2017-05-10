package com.example.genprog.model

import scala.util.Try

sealed trait Exp

case class Con(value: Float)  extends Exp
case class Var(value: Symbol) extends Exp

sealed trait BinOp extends Exp {
  def lhs: Exp
  def rhs: Exp
}

case class Add(lhs: Exp, rhs: Exp) extends BinOp
case class Sub(lhs: Exp, rhs: Exp) extends BinOp
case class Mul(lhs: Exp, rhs: Exp) extends BinOp
case class Div(lhs: Exp, rhs: Exp) extends BinOp

object Exp {
  def eval(exp: Exp, st: ST): Float = exp match {
    case Con(value)    => value
    case Var(name)     => st(name)
    case Add(lhs, rhs) => eval(lhs, st) + eval(rhs, st)
    case Sub(lhs, rhs) => eval(lhs, st) - eval(rhs, st)
    case Mul(lhs, rhs) => eval(lhs, st) * eval(rhs, st)
    // not throwing exceptions for simplicity
    case Div(lhs, rhs) => Try { eval(lhs, st) / eval(rhs, st) } getOrElse 1f
  }
}
