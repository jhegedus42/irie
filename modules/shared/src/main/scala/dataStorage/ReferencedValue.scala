package dataStorage

import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.generic.JsonCodec
import io.circe.syntax._

@JsonCodec
case class ReferencedValue[E <: Value[E]](
  entityValue: E,
  ref:         Ref[E] = Ref[E]()
  )
