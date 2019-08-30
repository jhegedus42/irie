package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.logic

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.Commands.{GetStateCommand, Insert, ShutdownActor, Update}
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.Responses.GetStateSnapshotActResp
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.state.{StateMapEntry, UntypedRef}
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.{JournalEntries, InsertEventEntries, UpdateEventEntries}
import app.shared.entity.entityValue.EntityValue

import scala.language.postfixOps
import scala.reflect.ClassTag

object PersistentActorImp {
  val as: ActorSystem = ActorSystem()

  def getActor(id: String ) = as.actorOf( props( id ) )

  def props(id: String ): Props =
    Props( new PersistentActorImp( id ) )
}

private[persistentActor] class PersistentActorImp(
  id: String)
    extends PersistentActor with ActorLogging {

  val state          = StateService()
  val commandHandler = MessageHandler()

  def getEntity[V <: EntityValue[V]: ClassTag](
    r: UntypedRef
  ): Option[StateMapEntry] = {
    state.getState.map.get( r )
  }

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case ShutdownActor =>
      println( "shutting down persistent actor" )
      context.stop( self )

    case command @ Insert( _ ) =>
      commandHandler.handleInsertEntityCommand( command )

    case command @ Update( _ ) =>
      commandHandler.handleUpdateEntityCommand( command )

    case GetStateCommand => {
      sender() ! GetStateSnapshotActResp(
        state.getState
      )
    }

  }

  private def applyEvent(
    event: JournalEntries
  ): Unit = event match {

    case InsertEventEntries( insertNewEntityCommand ) => {
      println(
        s"\n\nApplyEvent was called with CreateEntityEvent:\n$insertNewEntityCommand"
      )

      val newEntry: StateMapEntry =
        insertNewEntityCommand.newEntry //todo-now

      state.unsafeInsertStateEntry( newEntry )

    }

    case UpdateEventEntries( updateEntityCommand ) => {

      println(
        s"\n\nApplyEvent was called with UpdateEntityEvent:\n$updateEntityCommand"
      )

      val newEntry: StateMapEntry =
        updateEntityCommand.updatedEntry //todo-now

      state.unsafeInsertUpdatedEntity( newEntry )
      // todo-one-day => handle the "unsafe-ness"

    }

  }

  override def receiveRecover: Receive = {

    case evt: JournalEntries => {
      applyEvent( evt )
    }

    case RecoveryCompleted => {
      log.info(
        "Recovery completed \n\nState is:\n" + state.getState
      )
    }

  }
}