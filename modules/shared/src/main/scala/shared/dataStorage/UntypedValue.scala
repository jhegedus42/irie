package shared.dataStorage

import io.circe.{Encoder, Json}

case class UntypedValue(json: Json)

object UntypedValue {

  def getFromValue[V <: Value[V]: Encoder](v: V): UntypedValue = {
    val valueAsJSON: Json = implicitly[Encoder[V]].apply(v)
    UntypedValue(valueAsJSON)

  }
}
