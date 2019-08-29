package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.state.{
  StateMapEntry,
  StateMapSnapshot
}
import app.shared.entity.asString.EntityValueTypeAsString
import app.shared.utils.UUID_Utils.EntityIdentity

object Commands {
  sealed trait Command
  case object GetApplicationState extends Command
  case class Insert(newEntry:     StateMapEntry) extends Command
  case class Update(updatedEntry: StateMapEntry) extends Command
  case object Shutdown extends Command
}

case class DummyResponsePayload()

object Responses {
  sealed trait PersistentActorResponse
  case class GetFullApplicationState_Command_Response(state: StateMapSnapshot)
  case class InsertNewEntity_Command_Response(dummy:         DummyResponsePayload)
  case class UpdateEntity_Command_Response(areWeHappy:       DummyResponsePayload)
}

object Payloads {
  case class UntypedRefWithoutVersion(
      entityValueTypeAsString: EntityValueTypeAsString,
      entityIdentity:          EntityIdentity = EntityIdentity()
  )
}
