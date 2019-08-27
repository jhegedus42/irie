package app.server.httpServer.routes.logic.typeSafePersistenceHeaven.typelessHell.stupidPersistentActor

import app.server.httpServer.routes.logic.typeSafePersistenceHeaven.typelessHell.errorsInHell.AreWeHappyInTheUnderWorld
import app.server.httpServer.routes.logic.typeSafePersistenceHeaven.typelessHell.stupidPersistentActor.Errors.EntityUpdateVersionError
import app.server.httpServer.routes.logic.typeSafePersistenceHeaven.typelessHell.state.{ApplicationStateMap, StateChange}

private[typelessHell] object Responses {

  case class GetFullApplicationState_Command_Response(state: ApplicationStateMap )

  case class InsertNewEntity_Command_Response(stateChange: StateChange )

  case class UpdateEntity_Command_Response(areWeHappy: AreWeHappyInTheUnderWorld )

}
