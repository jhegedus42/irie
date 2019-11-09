package app.server.httpServer.routes.persistentActor

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, RecoveryCompleted}
import app.server.httpServer.routes.persistentActor.Commands._
import app.shared.state.UntypedEntityWithRef

import scala.language.postfixOps

private[persistentActor] class PersistentActorImpl(id: String)
    extends PersistentActor
    with ActorLogging {

  lazy val stateService = StateService()
  val commandHandler    = CommandMessgeHandler(stateService)

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case ShutdownActor =>
      println("shutting down persistent actor")
      context.stop(self)

    case command @ ResetStateCommand => {
      stateService.resetState()
      sender() ! "Minden kiraly!"
    }

    case command @ InsertNewEntityCommand(_) => {
      val res = commandHandler.handleInsert(command)
      // todo-next  => handle Insert ??? EVENT VS COMMAND ???
      // make the journal work ... for real / stop / start
      // make it really persist data
      sender() ! res
    }

    case command @ UpdateEntityCommand(_, _) =>
      val res: DidOperationSucceed =
        commandHandler.handleUpdate(command)
      sender() ! res

    case GetStateSnapshot => {
      println("handling the GetSnapshot command")
      sender() ! GetStateResponse(
        stateService.getState
      )
    }

  }

  private def applyEvent(event: EventToBeSavedIntoJournal): Unit =
    event match {

      case InsertEvent(insertEventPayload) => {
        println(
          s"\n\nApplyEvent was called with CreateEntityEvent:\n$insertEventPayload"
        )

        val newEntry: UntypedEntityWithRef =
          insertEventPayload.newEntry

//      val newState =
        // todo-later ... use some common handler , the one that handles the command too

//        stateService.getState.insertVirginEntity(newEntry)
//      stateService.setNewState(newState)

      }

      case UpdateEvent(insertEventPayload) => {

        println(
          s"\n\nApplyEvent was called with UpdateEntityEvent:\n$insertEventPayload"
        )

        // todo-later ... use some common handler , the one that handles the command too

        val newEntry: UntypedEntityWithRef =
          insertEventPayload.updatedEntry

//      val newState = stateService.getState.unsafeInsertUpdatedEntity(newEntry)
//      stateService.setNewState(newState)

      }

    }

  override def receiveRecover: Receive = {

    case evt:EventToBeSavedIntoJournal => {
      applyEvent(evt)
    }

    case RecoveryCompleted => {
      log.info(
        "Recovery completed \n\nState is:\n" + stateService.getState
      )
    }

  }
}
