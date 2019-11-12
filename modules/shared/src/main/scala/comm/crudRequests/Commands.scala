package comm.crudRequests

import dataStorage.stateHolder.UserMap

object Commands {

  sealed trait Command

  case object ShutDown extends Command

  case class GetUsersEntities(
    uuid: String,
    resp: Option[UserMap])
      extends Command
}
