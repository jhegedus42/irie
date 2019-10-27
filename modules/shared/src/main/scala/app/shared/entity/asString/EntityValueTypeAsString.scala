package app.shared.entity.asString

import app.shared.entity.entityValue.EntityType
import io.circe.generic.JsonCodec
import monocle.macros.Lenses

import scala.reflect.ClassTag
import monocle.macros.Lenses

import scala.reflect.ClassTag

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec

//@Lenses
//@JsonCodec
case class EntityValueTypeAsString(type_as_string: String)

object EntityValueTypeAsString {

  def getEntityValueTypeAsString[T <: EntityType[T]](
    implicit t: ClassTag[T]
  ): EntityValueTypeAsString =
    EntityValueTypeAsString(t.runtimeClass.getSimpleName)

}
