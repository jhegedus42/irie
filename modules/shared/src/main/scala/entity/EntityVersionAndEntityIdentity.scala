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
case class EntityVersionAndEntityIdentity[V <: EntityValueType[V]](
  version:  EntityVersion[V]  = EntityVersion[V](),
  identity: EntityIdentity[V] = EntityIdentity[V]())
