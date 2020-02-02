package shared.communication.persActorCommands.crudCMDs

import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import shared.communication.persActorCommands.Query
import shared.communication.{CanProvideRouteName,  RequestState}
import shared.dataStorage.relationalWrappers.{UnTypedReferencedValue, UntypedValue}

@JsonCodec
case class UpdateEntityPersActCmd(
  currentUnTypedReferencedValue: UnTypedReferencedValue,
  newUTPVal:                     UntypedValue,
  requestState:                  RequestState)
    extends Query

object UpdateEntityPersActCmd {
  implicit val users: CanProvideRouteName[UpdateEntityPersActCmd] =
    new CanProvideRouteName[UpdateEntityPersActCmd] {

      override def getRouteName: String =
        "UpdateEntityPersActCmd"
    }
}
