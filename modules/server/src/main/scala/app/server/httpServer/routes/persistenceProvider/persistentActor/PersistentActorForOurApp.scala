package app.server.httpServer.routes.persistenceProvider.persistentActor

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import app.server.httpServer.routes.persistenceProvider.persistentActor.Responses.{
  GetFullApplicationState_Command_Response,
  UpdateEntity_Command_Response
}
import app.server.httpServer.routes.persistenceProvider.persistentActor.commands.{
  GetFullApplicationState_Command,
  InsertNewEntity_Command,
  ShutdownActor_Command,
  UpdateEntity_Command
}
import app.server.httpServer.routes.persistenceProvider.persistentActor.events.{
  CreateEntityEvent,
  UpdateEntityEvent
}
import app.server.httpServer.routes.persistenceProvider.persistentActor.state.{
  ApplicationStateMap,
  ApplicationStateMapContainer,
  ApplicationStateMapEntry,
  StateChange,
  UntypedRef
}
import app.server.initialization.Config
import app.shared.entity.entityValue.EntityValue
import app.shared.utils.UUID_Utils.EntityIdentity

import scala.language.postfixOps
import scala.reflect.ClassTag

object PersistentActorForOurApp {
  val as: ActorSystem = ActorSystem()

  def getActor( id: String ) = as.actorOf( props( id ) )

  def props( id: String ): Props = Props( new PersistentActorForOurApp( id ) )
}

private[persistentActor] class PersistentActorForOurApp( id: String )
    extends PersistentActor with ActorLogging {

  val state = ApplicationStateMapContainer()

  def getEntity[V <: EntityValue[V]: ClassTag](
      r: UntypedRef
  ): Option[ApplicationStateMapEntry] = {
    state.getState.map.get( r )
  }

  override def persistenceId: String = id

  private object CommandHandlers {

    def handleUpdateEntityCommand( command: UpdateEntity_Command ): Unit = {
      val oldState = state.getState

      val event: events.UpdateEntityEvent = {
        events.UpdateEntityEvent( command )
      }

      persist( event ) { evt: events.UpdateEntityEvent =>
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

      // todo-one-day - handle errors related to "OCC" - "and such"
      //  so the following response is "not the best", putting it
      //  mildly

      val resp: Responses.UpdateEntity_Command_Response = UpdateEntity_Command_Response(
        (Right(stateChange))
      )

      sender() ! resp

    }

    def handleInsertEntityCommand( command: InsertNewEntity_Command ): Unit = {
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

      sender() ! Responses.InsertNewEntity_Command_Response( stateChange )

    }
  }

  override def receiveCommand: Receive = {
    case ShutdownActor_Command =>
      println( "shutting down persistent actor" )
      context.stop( self )

    case command @ InsertNewEntity_Command( _ ) =>
      CommandHandlers.handleInsertEntityCommand( command )

    case command @ UpdateEntity_Command( _ ) =>
      CommandHandlers.handleUpdateEntityCommand( command )

    case GetFullApplicationState_Command => {
      sender() ! GetFullApplicationState_Command_Response( state.getState )
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

    case UpdateEntityEvent( updateEntityCommand ) => {

      println(
        s"\n\nApplyEvent was called with UpdateEntityEvent:\n$updateEntityCommand"
      )

      val newEntry: ApplicationStateMapEntry = updateEntityCommand.updatedEntry

      state.unsafeInsertUpdatedEntity( newEntry )
      // todo-one-day => handle the "unsafe-ness"

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
