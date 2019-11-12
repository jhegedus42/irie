package dataStorage.stateHolder

import dataStorage.stateHolder.UsersEntities.RefMap
import dataStorage.{Ref, ReferencedValue, UnTypedRef, User, UserRef, Value}
import io.circe.{Json, KeyDecoder, KeyEncoder}
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec

//object UsersEntities {
//  type Key       = Ref[_]
//  type EntityMap = Map[Key, Json]
//}
object MapHolder{
}

case class UsersEntities() {

  type Key = UnTypedRef
  var refMap: RefMap = RefMap()

  def update(
    t:    Key,
    json: Json
  ): Unit = {
    val newMap = RefMap(refMap.map.updated(t, json))
    refMap = newMap
  }

  def getUserMap(ref: UserRef): UserMap = {
    val res  =
      refMap.map.filterKeys(_.userRef.uuid == ref.uuid)
    UserMap(ref,refMap.map.toList)
  }


  def insert(
    t:    Key,
    json: Json
  ): Unit = {
    val newMap  = refMap.map + ((t, json))
    refMap = RefMap(newMap)
  }

}

object UsersEntities {

  import dataStorage.stateHolder.UsersEntities.RefMap
  import dataStorage.{Ref, ReferencedValue, User, UserRef, Value}
  import io.circe.{Json, KeyEncoder}
  import io.circe.syntax._
  import io.circe.generic.auto._
  import io.circe.generic.JsonCodec

  case class RefMap(map:Map[UnTypedRef, Json]= Map[UnTypedRef,Json]())

}
