package mwittmann.example.withstacktrace

import mwittmann.example.Stacktrace
import mwittmann.example.Stacktrace._

// Same as the 'nostacktrace' parser, except stacktrace-enabled
object Parser {

  type Result[S] = Either[Stacktrace, S]

  sealed trait Ele
  case class EleList(v: List[Ele]) extends Ele
  case class EleMap(v: Map[String, Ele]) extends Ele {
    def get(key: String): Result[Ele] =
      v.get(key).map(Right.apply).getOrElse(makeLeftTrace(s"Map did not contain key $key.\n$v"))
  }
  case class EleInt(v: Int) extends Ele
  case class EleString(v: String) extends Ele
  case class EleBoolean(v: Boolean) extends Ele

  case class User(name: String, age: Int, alive: Boolean)

  def asMap(in: Ele): Result[EleMap] =
    in match {
      case m: EleMap => Right(m)
      case other => makeLeftTrace(s"Wrong type, wanted map, is: $other")
    }

  def asInt(in: Ele): Result[Int] =
    in match {
      case m: EleInt => Right(m.v)
      case other => makeLeftTrace(s"Wrong type, wanted map, is: $other")
    }

  def asBoolean(in: Ele): Result[Boolean] =
    in match {
      case m: EleBoolean => Right(m.v)
      case other => makeLeftTrace(s"Wrong type, wanted map, is: $other")
    }

  def asString(in: Ele): Result[String] =
    in match {
      case m: EleString => Right(m.v)
      case other => makeLeftTrace(s"Wrong type, wanted string, is: $other")
    }

  def parseUser(in: Ele)(implicit line: sourcecode.Line, file: sourcecode.File): Result[User] =
    updateTrace(parseUserI(in))
  def parseUserI(in: Ele): Result[User] = updateTrace {
    for {
      m <- asMap(in)
      name <- m.get("name").flatMap(asString)
      age <- m.get("age").flatMap(asInt)
      alive <- m.get("isAlive").flatMap(asBoolean)
      _ <- if (age < 10) makeLeftTrace("User is too young, must be >= 10 years") else Right(())
    } yield User(name, age, alive)
  }


  case class Order(total: Int, customer: User)

  def parseOrder(in: Ele)(implicit line: sourcecode.Line, file: sourcecode.File): Result[Order] =
    updateTrace(parseOrderI(in))
  def parseOrderI(in: Ele): Result[Order] = updateTrace {
    for {
      m     <- asMap(in)
      total  <- m.get("total").flatMap(asInt)
      user <- m.get("customer").flatMap(parseUser)
    } yield Order(total, user)
  }

}