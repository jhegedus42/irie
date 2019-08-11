package app.shared.dataModel.entity.refs

import app.shared.dataModel.entity.Entity.Entity
import app.shared.dataModel.entity.EntityTypeAsString
import app.shared.utils.UUID_Utils.{EntityUUID}
import monocle.macros.Lenses

import scala.reflect.ClassTag

@Lenses
case class TypedRef[T <: Entity[T]]( uuid:     EntityUUID, dataType: EntityTypeAsString )

object TypedRef {
  def getDefaultValue[T <: Entity[T]](implicit t:ClassTag[T]): TypedRef[T] ={
    val uuid:     EntityUUID         = EntityUUID()
    val dataType: EntityTypeAsString = EntityTypeAsString.make[T]
    TypedRef[T](uuid,dataType)
  }
}

//{
//
//  //  def isTypeCorrect(implicit t: ClassTag[T]) = dataType.isTypeCorrect[T]
//
//  //  def isTypeAndUUIDCorrect2(implicit t: ClassTag[T]): \/[SomeError_Trait, TypedRef[T]] = {
//  //    if (!uuid.isCorrect) return -\/(InvalidUUIDinURLError(s"uuid is $uuid"))
//  //
//  //    if (!dataType.isTypeCorrect[T]) return -\/(TypeError(s"problem with type ${t} or with uuid format ${uuid}"))
//  //    return \/-(this)
//  //  }
//
//}

//object TypedRef {
//
//  //  implicit def imp2[E <: Entity[E]]: Equal[TypedRef[E]] = Equal.equalBy(_.uuid.id)
//
//  //  implicit def instance[T <: Entity[T]]: UUIDCompare[TypedRef[T]] =
//  //    (x: TypedRef[T], y: TypedRef[T]) => x.uuid == y.uuid
//
//  //  def make[T <: Entity[T]]()(implicit t: ClassTag[T]): TypedRef[T] =
//  //    new TypedRef[T](EntityUUID(), EntityTypeAsString.make(t))
//
//  //  def makeWithUUID[T <: Entity[T]](uuid: EntityUUID)(implicit t: ClassTag[T]): TypedRef[T] =
//  //    new TypedRef[T](uuid, EntityTypeAsString.make(t))
//
//}
