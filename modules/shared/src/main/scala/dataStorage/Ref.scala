package dataStorage

import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec

import scala.ref.Reference




@JsonCodec
case class Ref[V <: Value[V]](
  uuid: String = java.util.UUID.randomUUID().toString)


