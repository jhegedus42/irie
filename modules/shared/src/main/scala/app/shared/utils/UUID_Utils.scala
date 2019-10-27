package app.shared.utils

import app.shared.entity.entityValue.EntityType
import io.circe.generic.JsonCodec
import monocle.macros.Lenses
import scalaz.\/
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec

import scala.util.matching.Regex

/**
  * Created by joco on 28/04/2017.
  */
object UUID_Utils {
//  @JsonCodec
@Lenses
case class EntityIdentity[V<:EntityType[V]](uuid: String=java.util.UUID.randomUUID().toString){
    def stripType:EntityIdentityUntyped=EntityIdentityUntyped(uuid)
  }

  val uuid00="00000000-0000-0000-0000-000000000000"
  val uuid01="00000000-0000-0000-0000-000000000001"
  val uuid02="00000000-0000-0000-0000-000000000002"
  val uuid03="00000000-0000-0000-0000-000000000003"
  val uuid04="00000000-0000-0000-0000-000000000004"


  @Lenses
  case class EntityIdentityUntyped(uuid:String=java.util.UUID.randomUUID().toString){
    def toTyped[V<:EntityType[V]]=EntityIdentity[V](uuid)
  }

}
