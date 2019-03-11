package app.shared.data.ref

import app.shared.data.model.TypeAsString
import app.shared.data.model.Entity.Entity
import app.shared.data.ref.uuid.{UUID, UUIDCompare}
import app.shared.{InvalidUUIDinURLError, SomeError_Trait, TypeError}
import monocle.macros.Lenses
import scalaz.{-\/, \/, \/-}

import scala.reflect.ClassTag

case class RefDyn(uuid: UUID, et: TypeAsString) {
  def toRef[E <: Entity: ClassTag](): \/[TypeError, Ref[E]] = {
    val eto = TypeAsString.make[E]
    if (et == eto) \/-(Ref(uuid, et))
    else -\/(TypeError("RefValDyn.toRefVal "))
  }

  def toRef_noClassTagNeeded[E <: Entity](
      expectedEntityType: TypeAsString): \/[TypeError, Ref[E]] = {
    if (et == expectedEntityType) \/-(Ref(uuid, et))
    else -\/(TypeError("RefValDyn.toRefVal "))
  }
//  def toRefUnsafe[E<:Entity]()=Ref[E](uuid,et)

}
object RefDyn {
  def make(et: TypeAsString) = RefDyn(UUID.random(), et)
}

@Lenses
case class Ref[T <: Entity](uuid: UUID = UUID.random(), dataType: TypeAsString) {

  //  to do get rid of this below, use make with circe,
  //  write decoder and encoder by hand, to use the make function
  //  ask this on gitter how to do this

  def isTypeCorrect(implicit t: ClassTag[T]) = dataType.isTypeCorrect[T]
//    def apply[T<:Entity](uuid: UUID = UUID.random(), entityType: EntityType): Ref[T] = new Ref(uuid, entityType)

  def isTypeAndUUIDCorrect2(implicit t: ClassTag[T]): \/[SomeError_Trait, Ref[T]] = {
    if (!uuid.isCorrect())   return -\/(InvalidUUIDinURLError(s"uuid is $uuid"))

    if (!dataType.isTypeCorrect[T] ) return -\/(TypeError(s"problem with type ${t} or with uuid format ${uuid}"))
    return \/-(this)
  }
}

//case class OwnerRef[T <: Entity](override val uuid: UUID = UUID.random(),  override  val entityType:EntityType) extends Ref(uuid,entityType) {
//
//}

object Ref {

  implicit def toRefDyn[E <: Entity](r: Ref[E]): RefDyn = RefDyn(r.uuid, r.dataType)

//  implicit val imp: Equal[Ref[User]] = Equal.equalBy(_.uuid)

  import scalaz._
  import Scalaz._
//  implicit val se: Equal[String] = Equal.equalA
  implicit def imp2[E<:Entity]: Equal[Ref[E]] = Equal.equalBy(_.uuid.id)

  implicit def instance[T <: Entity]: UUIDCompare[Ref[T]] =
    new UUIDCompare[Ref[T]] {
      override def isUUIDEq(x: Ref[T], y: Ref[T]) = x.uuid == y.uuid
    }

//    def make[T<:Entity[T]]()(implicit t:Typeable[T]): Ref[T] =
//      new Ref[T](UUID(), EntityType.make(t))

  def make[T <: Entity]()(implicit t: ClassTag[T]): Ref[T] =
    new Ref[T](UUID.random(), TypeAsString.make(t))

  def makeWithUUID[T <: Entity](uuid: UUID)(implicit t: ClassTag[T]): Ref[T] =
    new Ref[T](uuid, TypeAsString.make(t))

//    def apply[T<:Entity](uuid: UUID = UUID.random(), entityType: EntityType): Ref[T] = new Ref(uuid, entityType)

  //  import app.shared.apiAndModel.im.model.UUIDCompare.Pimper
  // ala http://alvinalexander.com/scala/scala-2.10-implicit-class-example

}
