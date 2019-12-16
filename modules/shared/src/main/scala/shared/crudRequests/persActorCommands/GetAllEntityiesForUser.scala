package shared.crudRequests.persActorCommands

import shared.crudRequests.{
  CanProvideRouteName,
  JSONConvertable
}
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import shared.dataStorage.RefToEntityOwningUser
import shared.dataStorage.stateHolder.UserMap

@JsonCodec
case class GetAllEntityiesForUser(
  par: RefToEntityOwningUser,
  res: Option[UserMap])
    extends PersActorCommand

object GetAllEntityiesForUser {

  implicit val users
    : CanProvideRouteName[GetAllEntityiesForUser] =
    new CanProvideRouteName[GetAllEntityiesForUser] {

      override def getRouteName: String =
        "GetAllEntityiesForUser"
    }

  implicit val jSONConvertable
    : JSONConvertable[GetAllEntityiesForUser] =
    new JSONConvertable[GetAllEntityiesForUser] {

      override def toJSON(
        v: GetAllEntityiesForUser
      ): String =
        v.asJson.spaces4

      override def fromJSONToObject(
        json: String
      ): GetAllEntityiesForUser = {
        val jsonParsed: Either[ParsingFailure, Json] =
          parse(json)
        val res1: Json = jsonParsed.toOption.get
        val decoder =
          implicitly[Decoder[GetAllEntityiesForUser]]
        val res2: Result[GetAllEntityiesForUser] =
          decoder.decodeJson(res1)
        res2.toOption.get
      }

    }
}
