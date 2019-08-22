package app.server.httpServer.routes.persistenceProvider.persistentActor

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import app.server.httpServer.routes.persistenceProvider.persistentActor.Responses.GetStateResult
import app.server.httpServer.routes.persistenceProvider.persistentActor.commands.{GetAllStateCommand, InsertNewEntityCommand, ShutdownActor}
import app.server.httpServer.routes.persistenceProvider.persistentActor.events.{CreateEntityEvent, UpdateEntityEvent}
import app.server.httpServer.routes.persistenceProvider.persistentActor.state.{ApplicationStateMap, ApplicationStateMapContainer, ApplicationStateMapEntry, UntypedRef}
import app.shared.entity.entityValue.EntityValue
import app.shared.initialization.Config
import app.shared.state._
import app.shared.utils.UUID_Utils.EntityIdentity

import scala.language.postfixOps
import scala.reflect.ClassTag

object WrappedPersistentActor {
  val as: ActorSystem = ActorSystem()

  def getActor( id: String ) = as.actorOf( props( id ) )

  def props( id: String ): Props = Props( new WrappedPersistentActor( id ) )
}

private[persistentActor] class WrappedPersistentActor( id: String )
    extends PersistentActor with ActorLogging {

  val state=ApplicationStateMapContainer()

  def getEntity[V <: EntityValue[V]: ClassTag](
      r: UntypedRef
  ): Option[ApplicationStateMapEntry] = {
    state.getState.map.get( r )
  }

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case ShutdownActor =>
      println( "shutting down persistent actor" )
      context.stop( self )

    case command @ InsertNewEntityCommand(
          newEntry: ApplicationStateMapEntry
        ) => {

      val oldState = state.getState

      val event: events.CreateEntityEvent = {
        events.CreateEntityEvent( command )
      }

      persist( event ) { evt: events.CreateEntityEvent =>
        applyEvent( evt )
      }

      val stateChange =
        StateChange( oldState, state.getState )

      println(
        "\n\n" +
          "ReceiveCommand was called\n" +
          "and matched the case 'InsertNewEntityCommand',\n" +
          "size of maps in StateChange:\n"
      )
      println( stateChange.getSizeOfMapsBeforeAndAfter )
      println()

      sender() ! Responses.InsertNewEntityCommandResponse( stateChange )

    }


    case GetAllStateCommand => {
      sender() ! GetStateResult( state.getState )
    }

  }

  private def applyEvent( event: events.Event ): Unit = event match {

    case CreateEntityEvent( insertNewEntityCommand ) => {
      println(
        s"\n\nApplyEvent was called with CreateEntityEvent:\n$insertNewEntityCommand"
      )

      val newEntry: ApplicationStateMapEntry = insertNewEntityCommand.newEntry

      state.unsafeInsertStateEntry( newEntry )

    }

    // todo-now update entity event
    case UpdateEntityEvent( updateEntityCommand ) => {

      println(
        s"\n\nApplyEvent was called with UpdateEntityEvent:\n$updateEntityCommand"
      )

      val newEntry: ApplicationStateMapEntry = updateEntityCommand.updatedEntry

      state.unsafeInsertUpdatedEntity( newEntry )


    }

  }


  override def receiveRecover: Receive = {

    case evt: events.Event => {
      applyEvent( evt )
    }

    case RecoveryCompleted => {
      log.info(
        "Recovery completed \n\nState is:\n" + state.getState
      )
    }

  }
}











