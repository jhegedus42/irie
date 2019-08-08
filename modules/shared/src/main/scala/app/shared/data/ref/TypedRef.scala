package app.shared.data.ref

import app.shared.data.model.TypeAsString
import app.shared.data.model.Entity.Entity
import app.shared.data.ref.UUID_Utils.{UUID, UUIDCompare}
import app.shared.{InvalidUUIDinURLError, SomeError_Trait, TypeError}
import monocle.macros.Lenses
import scalaz.{-\/, \/, \/-}

import scala.reflect.ClassTag


@Lenses
case class TypedRef[T <: Entity[T]](uuid: UUID = UUID.random(), dataType: TypeAsString) {


  def isTypeCorrect(implicit t: ClassTag[T]) = dataType.isTypeCorrect[T]

  def isTypeAndUUIDCorrect2(implicit t: ClassTag[T]): \/[SomeError_Trait, TypedRef[T]] = {
    if (!uuid.isCorrect) return -\/(InvalidUUIDinURLError(s"uuid is $uuid"))

    if (!dataType.isTypeCorrect[T]) return -\/(TypeError(s"problem with type ${t} or with uuid format ${uuid}"))
    return \/-(this)
  }

}


object TypedRef {



  import scalaz._
  import Scalaz._

  implicit def imp2[E <: Entity[E]]: Equal[TypedRef[E]] = Equal.equalBy(_.uuid.id)

  implicit def instance[T <: Entity[T]]: UUIDCompare[TypedRef[T]] =
    (x: TypedRef[T], y: TypedRef[T]) => x.uuid == y.uuid

  def make[T <: Entity[T]]()(implicit t: ClassTag[T]): TypedRef[T] =
    new TypedRef[T](UUID.random(), TypeAsString.make(t))

  def makeWithUUID[T <: Entity[T]](uuid: UUID)(implicit t: ClassTag[T]): TypedRef[T] =
    new TypedRef[T](uuid, TypeAsString.make(t))


}
