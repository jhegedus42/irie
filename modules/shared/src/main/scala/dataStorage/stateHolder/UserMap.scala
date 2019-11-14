package dataStorage.stateHolder

import dataStorage.stateHolder.EntityStorage.UntypedJSONMap
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec

import scala.collection.Map
import dataStorage.{
  Ref,
  ReferencedValue,
  UnTypedRef,
  RefToEntityOwningUser
}
import io.circe.Json

@JsonCodec
case class UserMap(
  user: RefToEntityOwningUser    = RefToEntityOwningUser(),
  list: List[(UnTypedRef, Json)] = List())
