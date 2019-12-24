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
import shared.dataStorage.{
  TypedReferencedValue,
  UnTypedReferencedValue,
  UntypedValue,
  UntypedVersionedValue,
  Value,
  VersionedValue
}

@JsonCodec
case class UpdateEntityPersActCmd(
  currentUnTypedReferencedValue: UnTypedReferencedValue,
  newUTPVal:                     UntypedValue,
  requestState:                  RequestState)
    extends PersActorCommand

object UpdateEntityPersActCmd {

  implicit val jSONConvertable
    : JSONConvertable[UpdateEntityPersActCmd] =
    new JSONConvertable[UpdateEntityPersActCmd] {

      override def toJSON(v: UpdateEntityPersActCmd): String =
        v.asJson.spaces4

      override def fromJSONToObject(
        json: String
      ): UpdateEntityPersActCmd = {
        val jsonParsed: Either[ParsingFailure, Json] =
          parse(json)

        val res1: Json = jsonParsed.toOption.get

        val decoder =
          implicitly[Decoder[UpdateEntityPersActCmd]]

        val res2: Result[UpdateEntityPersActCmd] =
          decoder.decodeJson(res1)

        res2.toOption.get
      }

    }

  implicit val users: CanProvideRouteName[UpdateEntityPersActCmd] =
    new CanProvideRouteName[UpdateEntityPersActCmd] {

      override def getRouteName: String =
        "UpdateEntityPersActCmd"
    }
}
