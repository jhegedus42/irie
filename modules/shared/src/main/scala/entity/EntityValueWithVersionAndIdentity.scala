package entity

import dataModel.EntityValueType

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
//import io.circe.generic.auto._
import io.circe.generic.JsonCodec
import io.circe.syntax._

@JsonCodec
case class EntityValueWithVersionAndIdentity[E <: EntityValueType[E]](
  entityValue: E,
  ref:         EntityVersionAndEntityIdentity[E] = EntityVersionAndEntityIdentity[E]())
