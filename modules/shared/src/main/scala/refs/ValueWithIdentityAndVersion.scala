package refs

import dataModel.EntityType
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._

@JsonCodec
case class ValueWithIdentityAndVersion[E <: EntityType[E]](
  entityValue: E,
  ref:         IdentityAndVersion[E]) {

  def bumpVersion: ValueWithIdentityAndVersion[E] =
    this.copy(ref = this.ref.bumpVersion)

}
