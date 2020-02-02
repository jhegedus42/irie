package shared.communication.persActorCommands.crudCMDs

import io.circe.Decoder.Result
import io.circe._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import shared.communication.persActorCommands.Query
import shared.communication.{CanProvideRouteName }
import shared.dataStorage.model.{PWDHashed, PWDNotHashed}
import shared.dataStorage.relationalWrappers.RefToEntityOwningUser
import shared.dataStorage.stateHolder.UserMap

@JsonCodec
case class GetAllEntityiesForUserPersActCmd(
  par: RefToEntityOwningUser,
  res: Option[UserMap]
) extends Query

object GetAllEntityiesForUserPersActCmd {

  implicit val users
    : CanProvideRouteName[GetAllEntityiesForUserPersActCmd] =
    new CanProvideRouteName[GetAllEntityiesForUserPersActCmd] {

      override def getRouteName: String =
        "GetAllEntityiesForUser"
    }

}
