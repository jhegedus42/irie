package dataModel

import com.sun.jdi.Value
import entity.Entity
import io.circe.generic.JsonCodec
import shapeless.T

trait EntityValueType[+T <: EntityValueType[T]] {}

object EntityValueType {
  implicit def makeFromValue[V <: EntityValueType[V]](
    v: V
  ): Entity[V] =
    Entity(v)
}
