package app.server.httpServer.routes.persistenceProvider.persistentActor

import app.server.httpServer.routes.persistenceProvider.persistentActor.Errors.EntityUpdateVersionError
import app.server.httpServer.routes.persistenceProvider.persistentActor.state.{ApplicationStateMap, StateChange}

private[persistenceProvider] object Responses {

  case class GetStateResult( state: ApplicationStateMap )

  case class InsertNewEntityCommandResponse( stateChange: StateChange )

  case class UpdateEntityResponse(
      result: Either[EntityUpdateVersionError, StateChange]
  )

}
