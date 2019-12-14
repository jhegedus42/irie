package comm.crudRequests.persActorCommands

import comm.crudRequests.{
  CanProvideRouteName,
  JSONConvertable
}
import dataStorage.{
  RefToEntityOwningUser,
  TypedReferencedValue,
  UnTypedReferencedValue,
  Value
}
import dataStorage.stateHolder.UserMap
import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import shapeless.Typeable

sealed trait RequestState

@JsonCodec
case class RequestIsOnItsWayTowardsServer()
    extends RequestState

@JsonCodec
case class RequestSuccessfullyReturned()
    extends RequestState

@JsonCodec
case class RequestReturnedWithError(
  errorDescription: String)
    extends RequestState

/**
  * @param unTypedReferencedValue
  * @param res
  */

@JsonCodec
case class InsertEntityIntoDataStore(
  unTypedReferencedValue: UnTypedReferencedValue,
  res:                    RequestState)
    extends PersActorCommand

object InsertEntityIntoDataStore {

  def fromReferencedValue[V <: Value[V]](
    r: TypedReferencedValue[V]
  )(
    implicit
    enc:      Encoder[TypedReferencedValue[V]],
    typeable: Typeable[V]
  ): InsertEntityIntoDataStore = {

    val unTypedReferencedValue =
      UnTypedReferencedValue.fromTypedReferencedValue[V](r)

    InsertEntityIntoDataStore(
      unTypedReferencedValue,
      RequestIsOnItsWayTowardsServer()
    )
  }

  implicit val jSONConvertable
    : JSONConvertable[InsertEntityIntoDataStore] =
    new JSONConvertable[InsertEntityIntoDataStore] {

      override def getJSON(
        v: InsertEntityIntoDataStore
      ): String =
        v.asJson.spaces4

      override def getObject(
        json: String
      ): InsertEntityIntoDataStore = {
        val jsonParsed: Either[ParsingFailure, Json] =
          parse(json)
        val res1: Json = jsonParsed.toOption.get
        val decoder =
          implicitly[Decoder[InsertEntityIntoDataStore]]
        val res2: Result[InsertEntityIntoDataStore] =
          decoder.decodeJson(res1)
        res2.toOption.get
      }

    }

  implicit val users
    : CanProvideRouteName[InsertEntityIntoDataStore] =
    new CanProvideRouteName[InsertEntityIntoDataStore] {

      override def getRouteName: String =
        "InsertEntityIntoDataStore"
    }

}
