package shared.crudRESTCallCommands.persActorCommands

import shared.crudRESTCallCommands.{CanProvideRouteName, JSONConvertable, RequestIsOnItsWayTowardsServer, RequestState}
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import shapeless.Typeable
import shared.dataStorage.model.Value
import shared.dataStorage.relationalWrappers.{TypedReferencedValue, UnTypedReferencedValue}

/**
  * @param unTypedReferencedValue
  * @param res
  */

@JsonCodec
case class InsertEntityPersActCmd(
  unTypedReferencedValue: UnTypedReferencedValue,
  res:                    RequestState)
    extends PersActorCommand

object InsertEntityPersActCmd {

  def fromReferencedValue[V <: Value[V]: Encoder](
    r: TypedReferencedValue[V]
  )(
    implicit
    enc:      Encoder[TypedReferencedValue[V]],
    typeable: Typeable[V]
  ): InsertEntityPersActCmd = {

    val unTypedReferencedValue =
      UnTypedReferencedValue.fromTypedReferencedValue[V](r)

    InsertEntityPersActCmd(
      unTypedReferencedValue,
      RequestIsOnItsWayTowardsServer()
    )
  }

  implicit val jSONConvertable
    : JSONConvertable[InsertEntityPersActCmd] =
    new JSONConvertable[InsertEntityPersActCmd] {

      override def toJSON(v: InsertEntityPersActCmd): String =
        v.asJson.spaces4

      override def fromJSONToObject(
        json: String
      ): InsertEntityPersActCmd = {
        val jsonParsed: Either[ParsingFailure, Json] =
          parse(json)

        val res1: Json = jsonParsed.toOption.get

        val decoder =
          implicitly[Decoder[InsertEntityPersActCmd]]

        val res2: Result[InsertEntityPersActCmd] =
          decoder.decodeJson(res1)

        res2.toOption.get
      }

    }

  implicit val users: CanProvideRouteName[InsertEntityPersActCmd] =
    new CanProvideRouteName[InsertEntityPersActCmd] {

      override def getRouteName: String =
        "InsertEntityIntoDataStore"
    }

}
