package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor.commands.state.ApplicationStateMap

case class DummyResponsePayload()

sealed trait PersistentActorResponse

  case class GetFullApplicationState_Command_Response(state: ApplicationStateMap )

  case class InsertNewEntity_Command_Response(dummy: DummyResponsePayload)

  case class UpdateEntity_Command_Response(areWeHappy: DummyResponsePayload)




