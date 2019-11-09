package refs

import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import refs.asString.EntityValueTypeAsString

import scala.reflect.ClassTag

//@Lenses
@JsonCodec
case class EntityWithRef[E <: EntityType[E]](
  entityValue: E,
  toRef:       RefToEntityWithVersion[E]) {

  def bumpVersion: EntityWithRef[E] =
    this.copy(toRef = this.toRef.bumpVersion)

}

object EntityWithRef {

  /**
    * @param v
    * @tparam V
    * @return Entity with random UUID and Version 0.
    */
  def makeFromValue[V <: EntityType[V]: ClassTag](
    v: V
  )(
    implicit d: Decoder[V]
  ): EntityWithRef[V] = {
    val e: EntityValueTypeAsString =
      EntityValueTypeAsString.getEntityValueTypeAsString[V]
    val tr: RefToEntityWithVersion[V] =
      RefToEntityWithVersion[V](e )

    EntityWithRef(v, tr)
  }

}
