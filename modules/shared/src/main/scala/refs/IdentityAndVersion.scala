package refs

import dataModel.EntityType
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._

import scala.reflect.ClassTag

//@Lenses
//@JsonCodec
case class IdentityAndVersion[T <: EntityType[T]](
  entityIdentity: Identity[T] = Identity[T](),
  entityVersion:  Version[T]  = Version[T]()) {
  def bumpVersion: IdentityAndVersion[T] =
    this.copy(entityVersion = entityVersion.bumpVersion())
}

object IdentityAndVersion {
  implicit def fromEntityIdentity[T <: EntityType[T]: ClassTag]
  ( identity: Identity[T] ): IdentityAndVersion[T] = { IdentityAndVersion( identity ) }

}
