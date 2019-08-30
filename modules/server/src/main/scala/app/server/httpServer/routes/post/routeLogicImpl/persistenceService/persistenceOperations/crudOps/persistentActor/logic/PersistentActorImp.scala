package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.persistentActor.logic

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.persistentActor.data.Commands.{
  GetStateSnapshot,
  Insert,
  ShutdownActor,
  Update
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.persistentActor.data.Responses.GetStateResponse
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.persistentActor.data.state.{
  StateMapEntry,
  UntypedRef
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.persistentActor.data.{
  EventToBeSavedIntoJournal,
  InsertEvent,
  UpdateEvent
}
import app.shared.entity.entityValue.EntityValue

import scala.language.postfixOps
import scala.reflect.ClassTag

object PersistentActorImp {
  val as: ActorSystem = ActorSystem()

  def getActor(id: String) = as.actorOf(props(id))

  def props(id: String): Props =
    Props(new PersistentActorImp(id))
}

private[persistentActor] class PersistentActorImp(id: String)
    extends PersistentActor
    with ActorLogging {

  val stateService   = StateService()
  val commandHandler = CommandMessgeHandler()

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case ShutdownActor =>
      println("shutting down persistent actor")
      context.stop(self)

    case command @ Insert(_) =>
      commandHandler.handleInsert(command)

    case command @ Update(_) =>
      commandHandler.handleUpdate(command)

    case GetStateSnapshot => {
      sender() ! GetStateResponse(
        stateService.getState
      )
    }

  }

  private def applyEvent(
      event: EventToBeSavedIntoJournal
  ): Unit = event match {

    case InsertEvent(insertEventPayload) => {
      println(
        s"\n\nApplyEvent was called with CreateEntityEvent:\n$insertEventPayload"
      )

      val newEntry: StateMapEntry =
        insertEventPayload.newEntry

      val newState = stateService.getState.unsafeInsertStateEntry(newEntry)
      stateService.setNewState(newState)

    }

    case UpdateEvent(insertEventPayload) => {

      println(
        s"\n\nApplyEvent was called with UpdateEntityEvent:\n$insertEventPayload"
      )

      val newEntry: StateMapEntry =
        insertEventPayload.updatedEntry

      val newState = stateService.getState.unsafeInsertUpdatedEntity(newEntry)
      stateService.setNewState(newState)

    }

  }

  override def receiveRecover: Receive = {

    case evt: EventToBeSavedIntoJournal => {
      applyEvent(evt)
    }

    case RecoveryCompleted => {
      log.info(
        "Recovery completed \n\nState is:\n" + stateService.getState
      )
    }

  }
}
