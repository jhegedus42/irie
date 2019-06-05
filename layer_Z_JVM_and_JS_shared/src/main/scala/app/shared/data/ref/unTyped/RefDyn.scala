package app.shared.data.ref.unTyped

import app.shared.TypeError
import app.shared.data.model.Entity.Entity
import app.shared.data.model.TypeAsString
import app.shared.data.ref.Ref
import app.shared.data.ref.uuid.UUID
import scalaz.{-\/, \/, \/-}

import scala.reflect.ClassTag

object RefDyn {
  def make(et: TypeAsString) = RefDyn(UUID.random(), et)
}

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