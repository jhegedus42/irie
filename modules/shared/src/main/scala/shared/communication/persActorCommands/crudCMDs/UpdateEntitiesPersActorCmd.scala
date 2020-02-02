package shared.communication.persActorCommands.crudCMDs

import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import shared.communication.persActorCommands.Query
import shared.communication.{CanProvideRouteName,  RequestState}

@JsonCodec
case class UpdateEntitiesPersActorCmd(
  cmds: List[UpdateEntityPersActCmd],requestState:RequestState)
    extends Query

object UpdateEntitiesPersActorCmd {
  implicit val routeNameProvider
    : CanProvideRouteName[UpdateEntitiesPersActorCmd] =
    new CanProvideRouteName[UpdateEntitiesPersActorCmd] {

      override def getRouteName: String =
        "UpdateEntitiesPersActorCmd"
    }
}
