package dataStorage

import io.circe.Json
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec
import shapeless.Typeable

import scala.ref.Reference
import scala.reflect.ClassTag

import io.circe._, io.circe.generic.semiauto._

@JsonCodec
case class Ref[V <: Value[V]](unTypedRef: UnTypedRef=UnTypedRef())

@JsonCodec
case class UnTypedRef(
  uuid:    String  = java.util.UUID.randomUUID().toString,
  userRef: UserRef = UserRef())

