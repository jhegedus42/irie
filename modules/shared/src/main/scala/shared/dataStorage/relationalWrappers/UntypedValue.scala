package shared.dataStorage.relationalWrappers

import io.circe.generic.JsonCodec
import io.circe.{Encoder, Json}
import shared.dataStorage.model.Value

@JsonCodec
case class UntypedValue(json: Json)

object UntypedValue {

  def getFromValue[V <: Value[V]: Encoder](v: V): UntypedValue = {
    val valueAsJSON: Json = implicitly[Encoder[V]].apply(v)
    UntypedValue(valueAsJSON)

  }
}
