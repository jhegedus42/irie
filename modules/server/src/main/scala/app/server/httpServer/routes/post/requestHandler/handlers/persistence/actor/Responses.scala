package app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor

import app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor.state.{ApplicationStateMap, ApplicationStateMapEntry, StateChange}

sealed trait PersistentActorResponse

  case class GetFullApplicationState_Command_Response(state: ApplicationStateMap )

  case class InsertNewEntity_Command_Response(stateChange: StateChange )

  case class UpdateEntity_Command_Response(areWeHappy: AreWeHappyInTheUnderWorld )




