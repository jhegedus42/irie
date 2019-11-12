package dataStorage

import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec

import scala.ref.Reference

case class UserRef(uuid:String=java.util.UUID.randomUUID().toString)

@JsonCodec
case class Ref[V <: Value[V]](
  uuid: String = java.util.UUID.randomUUID().toString,
  user: UserRef=UserRef()){
  def ref2tuple: (String, String) =(this.uuid,user.uuid)
  def tuple2ref(t:(String, String)) = {
    Ref[V](t._1,UserRef(t._2))
  }
}
