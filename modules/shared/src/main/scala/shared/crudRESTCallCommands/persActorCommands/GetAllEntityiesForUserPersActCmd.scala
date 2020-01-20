package shared.crudRESTCallCommands.persActorCommands

import shared.crudRESTCallCommands.{CanProvideRouteName, JSONConvertable}
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import shared.dataStorage.relationalWrappers.RefToEntityOwningUser
import shared.dataStorage.stateHolder.UserMap

@JsonCodec
case class GetAllEntityiesForUserPersActCmd(
  par: RefToEntityOwningUser,
  res: Option[UserMap])
    extends PersActorCommand

object GetAllEntityiesForUserPersActCmd {

  implicit val users
    : CanProvideRouteName[GetAllEntityiesForUserPersActCmd] =
    new CanProvideRouteName[GetAllEntityiesForUserPersActCmd] {

      override def getRouteName: String =
        "GetAllEntityiesForUser"
    }

  implicit val jSONConvertable
    : JSONConvertable[GetAllEntityiesForUserPersActCmd] =
    new JSONConvertable[GetAllEntityiesForUserPersActCmd] {

      override def toJSON(
        v: GetAllEntityiesForUserPersActCmd
      ): String =
        v.asJson.spaces4

      override def fromJSONToObject(
        json: String
      ): GetAllEntityiesForUserPersActCmd = {
        val jsonParsed: Either[ParsingFailure, Json] =
          parse(json)
        val res1: Json = jsonParsed.toOption.get
        val decoder =
          implicitly[Decoder[GetAllEntityiesForUserPersActCmd]]
        val res2: Result[GetAllEntityiesForUserPersActCmd] =
          decoder.decodeJson(res1)
        res2.toOption.get
      }

    }
}
