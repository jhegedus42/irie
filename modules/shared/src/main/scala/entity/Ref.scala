package entity
import dataModel.EntityValueType

/**
  * Created by joco on 28/04/2017.
  */

import dataModel.EntityValueType
import io.circe._

import scala.reflect.ClassTag

import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec

@JsonCodec
case class Identity[V <: EntityValueType[V]](
  uuid: String = java.util.UUID.randomUUID().toString)




@JsonCodec
case class Ref[V <: EntityValueType[V]](
  version:  Version[V]  = Version[V](),
  identity: Identity[V] = Identity[V]())