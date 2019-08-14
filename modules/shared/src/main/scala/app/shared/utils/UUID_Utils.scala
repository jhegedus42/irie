package app.shared.utils

import scalaz.\/

import scala.util.matching.Regex

/**
  * Created by joco on 28/04/2017.
  */
object UUID_Utils {
  case class IdentityOfEntity(uuid: String=java.util.UUID.randomUUID().toString)

}
