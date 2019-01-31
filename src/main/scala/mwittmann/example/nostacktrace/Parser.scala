package mwittmann.example.nostacktrace

object Parser {

  // Our very simple little sealed trait of 'tokens'
  sealed trait Ele
  case class EleList(v: List[Ele]) extends Ele
  case class EleMap(v: Map[String, Ele]) extends Ele {
    def get(key: String): Either[String, Ele] =
      v.get(key).map(Right.apply).getOrElse(Left(s"Map did not contain key $key.\n$v"))
  }
  case class EleInt(v: Int) extends Ele
  case class EleString(v: String) extends Ele
  case class EleBoolean(v: Boolean) extends Ele

  // Convenience methods for extracting things from tokens, returning an error (Left) if the thing doesn't contain
  // what we expected.
  def asMap(in: Ele): Either[String, EleMap] = in match {
    case m: EleMap => Right(m)
    case other => Left(s"Wrong type, wanted map, is: $other")
  }

  def asInt(in: Ele): Either[String, Int] = in match {
    case m: EleInt => Right(m.v)
    case other => Left(s"Wrong type, wanted map, is: $other")
  }

  def asBoolean(in: Ele): Either[String, Boolean] = in match {
    case m: EleBoolean => Right(m.v)
    case other => Left(s"Wrong type, wanted map, is: $other")
  }

  def asString(in: Ele): Either[String, String] = in match {
    case m: EleString => Right(m.v)
    case other => Left(s"Wrong type, wanted string, is: $other")
  }

  // Parse a 'User' with atomic fields from an EleMap
  case class User(name: String, age: Int, alive: Boolean)
  def parseUser(in: Ele): Either[String, User] =
    for {
      m     <- asMap(in)
      name  <- m.get("name").flatMap(asString)
      age   <- m.get("age").flatMap(asInt)
      alive <- m.get("isAlive").flatMap(asBoolean)
    } yield User(name, age, alive)

  // Parse an order that contains an atom int as well as a User object
  case class Order(total: Int, customer: User)
  def parseOrder(in: Ele): Either[String, Order] = for {
    m     <- asMap(in)
    total  <- m.get("total").flatMap(asInt)
    user <- m.get("customer").flatMap(parseUser)
  } yield Order(total, user)

}