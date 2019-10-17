package app.shared.utils

import io.circe.generic.JsonCodec
import monocle.macros.Lenses
import scalaz.\/

import scala.util.matching.Regex

/**
  * Created by joco on 28/04/2017.
  */
object UUID_Utils {
  @Lenses
  case class EntityIdentity(uuid: String=java.util.UUID.randomUUID().toString)

  val uuid00="00000000-0000-0000-0000-000000000000"
  val uuid01="00000000-0000-0000-0000-000000000001"
  val uuid02="00000000-0000-0000-0000-000000000002"
  val uuid03="00000000-0000-0000-0000-000000000003"
  val uuid04="00000000-0000-0000-0000-000000000004"



}
