package shared.crudRESTCallCommands.persActorCommands

import shared.crudRESTCallCommands.{
  CanProvideRouteName,
  JSONConvertable,
  RequestIsOnItsWayTowardsServer,
  RequestState
}
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import shapeless.Typeable

@JsonCodec
case class UpdateEntitiesPersActorCmd(
  cmds: List[UpdateEntityPersActCmd],requestState:RequestState)
    extends PersActorCommand

object UpdateEntitiesPersActorCmd {

  implicit val jSONConvertable
    : JSONConvertable[UpdateEntitiesPersActorCmd] =
    new JSONConvertable[UpdateEntitiesPersActorCmd] {

      override def toJSON(v: UpdateEntitiesPersActorCmd): String =
        v.asJson.spaces4

      override def fromJSONToObject(
        json: String
      ): UpdateEntitiesPersActorCmd = {
        val jsonParsed: Either[ParsingFailure, Json] =
          parse(json)

        val res1: Json = jsonParsed.toOption.get

        val decoder =
          implicitly[Decoder[UpdateEntitiesPersActorCmd]]

        val res2: Result[UpdateEntitiesPersActorCmd] =
          decoder.decodeJson(res1)

        res2.toOption.get
      }

    }

  implicit val routeNameProvider
    : CanProvideRouteName[UpdateEntitiesPersActorCmd] =
    new CanProvideRouteName[UpdateEntitiesPersActorCmd] {

      override def getRouteName: String =
        "UpdateEntitiesPersActorCmd"
    }
}
