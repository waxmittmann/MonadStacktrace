package mwittmann.example.nostacktrace

import mwittmann.example.nostacktrace.Parser._

// EXACTLY the same as the withstacktrace version, except we import the parser without stacktrace
object Main {

  def main(args: Array[String]): Unit = {

    val rawOrder =
      EleMap(Map(
        "total" -> EleInt(123),
        "customer" -> EleMap(Map(
          "name" -> EleString("Joe"),
          "age" -> EleInt(13),
          "isAlive" -> EleBoolean(true)
        ))
      ))

    println(parseOrder(rawOrder) + "\n\n\n")

    val rawOrderCustomerMissingAge =
      EleMap(Map(
        "total" -> EleInt(123),
        "customer" -> EleMap(Map(
          "name" -> EleString("Joe"),
          "isAlive" -> EleBoolean(true)
        ))
      ))

    println(parseOrder(rawOrderCustomerMissingAge) + "\n\n\n")

    val rawOrderCustomerAgeWrongType =
      EleMap(Map(
        "total" -> EleInt(123),
        "customer" -> EleMap(Map(
          "name" -> EleString("Joe"),
          "age" -> EleString("13"),
          "isAlive" -> EleBoolean(true)
        ))
      ))

    println(parseOrder(rawOrderCustomerAgeWrongType) + "\n\n\n")

    val rawOrderCustomerTooYoung =
      EleMap(Map(
        "total" -> EleInt(123),
        "customer" -> EleMap(Map(
          "name" -> EleString("Joe"),
          "age" -> EleInt(6),
          "isAlive" -> EleBoolean(true)
        ))
      ))

    println(parseOrder(rawOrderCustomerTooYoung) + "\n\n\n")
  }

}