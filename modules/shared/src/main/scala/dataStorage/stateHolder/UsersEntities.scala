package dataStorage.stateHolder

import dataStorage.{Ref, ReferencedValue, User, Value}
import io.circe._
import io.circe.syntax._
import io.circe.generic.JsonCodec




case class UsersEntities() {

  var map = Map[(String, String), Json]()

  def update(
    t:    (String, String),
    json: Json
  ): Unit = {
    val newMap: Map[(String, String), Json] = map.updated(t, json)
    map = newMap
  }

  def getUserMap(uuid:String): UserMap = {
    val res: Map[(String, String), Json] =
      map.filterKeys(_._1 == uuid)
    val list=map.toList
    UserMap(list)
  }

  def insert(
    t:    (String, String),
    json: Json
  ): Unit = {
    val newMap: Map[(String, String), Json] = map + ((t, json))
    map = newMap
  }

}
