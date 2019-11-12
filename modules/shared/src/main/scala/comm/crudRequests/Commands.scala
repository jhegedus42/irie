package comm.crudRequests

import dataStorage.UserRef
import dataStorage.stateHolder.UserMap
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec
import dataStorage.UserRef
import dataStorage.stateHolder.UserMap

sealed trait Command

case object ShutDown extends Command

@JsonCodec
case class GetAllEntityiesForUser(
  par: UserRef,
  res: Option[UserMap])
    extends Command


trait RouteName[V]{
  def getRouteName:String
}

object RouteName {
  implicit val users = new RouteName[GetAllEntityiesForUser] {
    override def getRouteName: String = "GetAllEntityiesForUser"
  }
}

trait JSONConvertable[V]{
  def getJSON(v:V):String
  def getObject(json:String) : V
}

object JSONConvertable {

}
