package app.shared.data.ref.unTyped

import app.shared.TypeError
import app.shared.data.model.Entity.Entity
import app.shared.data.model.TypeAsString
import app.shared.data.ref.TypedRef
import app.shared.data.ref.UUID_Utils.UUID
import scalaz.{-\/, \/, \/-}

import scala.reflect.ClassTag

object NotTypeSafeRef {
  def make(et: TypeAsString) = NotTypeSafeRef(UUID.random(), et)
}

case class NotTypeSafeRef(uuid: UUID, et: TypeAsString) {
  def toRef[E <: Entity: ClassTag](): \/[TypeError, TypedRef[E]] = {
    val eto = TypeAsString.make[E]
    if (et == eto) \/-(TypedRef(uuid, et))
    else -\/(TypeError("RefValDyn.toRefVal "))
  }

  def toRef_noClassTagNeeded[E <: Entity](
      expectedEntityType: TypeAsString): \/[TypeError, TypedRef[E]] = {
    if (et == expectedEntityType) \/-(TypedRef(uuid, et))
    else -\/(TypeError("RefValDyn.toRefVal "))
  }
//  def toRefUnsafe[E<:Entity]()=Ref[E](uuid,et)

}