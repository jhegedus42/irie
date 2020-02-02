package shared.communication.persActorCommands.crudCMDs

import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import shapeless.Typeable
import shared.communication.persActorCommands.Query
import shared.communication.{CanProvideRouteName,  RequestIsOnItsWayTowardsServer, RequestState}
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
    extends Query

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

  implicit val users: CanProvideRouteName[InsertEntityPersActCmd] =
    new CanProvideRouteName[InsertEntityPersActCmd] {

      override def getRouteName: String =
        "InsertEntityIntoDataStore"
    }

}
