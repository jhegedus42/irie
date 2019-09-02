package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.logic

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.Commands.{
  GetStateSnapshot,
  InsertCommand,
  ShutdownActor,
  Update
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.Responses.GetStateResponse
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state.{
  StateMapEntry,
  UntypedRef
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.{
  EventToBeSavedIntoJournal,
  InsertEvent,
  UpdateEvent
}
import app.shared.entity.entityValue.EntityValue

import scala.language.postfixOps
import scala.reflect.ClassTag

private[persistentActor] class PersistentActorImpl(id: String)
    extends PersistentActor
    with ActorLogging {

  val stateService   = StateService()
  val commandHandler = CommandMessgeHandler()

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case ShutdownActor =>
      println("shutting down persistent actor")
      context.stop(self)

    case command @ InsertCommand(_) =>
      commandHandler.handleInsert(command)

    case command @ Update(_) =>
      commandHandler.handleUpdate(command)

    case GetStateSnapshot => {
      println("handling the GetSnapshot command")
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
