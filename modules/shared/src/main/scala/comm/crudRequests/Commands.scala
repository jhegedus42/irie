package comm.crudRequests

import dataStorage.UserRef
import dataStorage.stateHolder.UserMap
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.generic.JsonCodec
import io.circe._
import dataStorage.UserRef
import dataStorage.stateHolder.UserMap
import io.circe.Decoder.Result
import io.circe.parser._
import jdk.nashorn.internal.parser.JSONParser

sealed trait Command

case object ShutDown extends Command

@JsonCodec
case class GetAllEntityiesForUser(
  par: UserRef,
  res: Option[UserMap])
    extends Command

object GetAllEntityiesForUser {
  implicit val jSONConvertable =
    new JSONConvertable[GetAllEntityiesForUser] {
      override def getJSON(v: GetAllEntityiesForUser): String =
        v.asJson.toString()

      override def getObject(json: String): GetAllEntityiesForUser = {
        val jsonParsed: Either[ParsingFailure, Json] = parse(json)
        val res1:       Json                         = jsonParsed.toOption.get
        val decoder = implicitly[Decoder[GetAllEntityiesForUser]]
        val res2: Result[GetAllEntityiesForUser] =
          decoder.decodeJson(res1)
        res2.toOption.get
      }
    }
}

trait RouteName[V] {
  def getRouteName: String
}

object RouteName {
  implicit val users = new RouteName[GetAllEntityiesForUser] {
    override def getRouteName: String = "GetAllEntityiesForUser"
  }
}

trait JSONConvertable[V] {
  def getJSON(v:      V):      String
  def getObject(json: String): V
}

object JSONConvertable {}
