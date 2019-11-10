package entity

import dataModel.EntityValueType
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._

import scala.reflect.ClassTag

@JsonCodec
case class Entity[E <: EntityValueType[E]](
  entityValue: E,
  ref:         Ref[E] = Ref[E]())

object Entity {}
