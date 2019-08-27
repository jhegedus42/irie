package app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor.state.refs.UntypedRef
import app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor.state.{ApplicationStateMapContainer, ApplicationStateMapEntry, StateChange}
import app.shared.entity.entityValue.EntityValue

import scala.language.postfixOps
import scala.reflect.ClassTag

object PersistentActorForOurApp {
  val as: ActorSystem = ActorSystem()

  def getActor( id: String ) = as.actorOf( props( id ) )

  def props( id: String ): Props = Props( new PersistentActorForOurApp( id ) )
}

private[actor] class PersistentActorForOurApp(id: String )
    extends PersistentActor with ActorLogging {

  val state = ApplicationStateMapContainer()

  def getEntity[V <: EntityValue[V]: ClassTag](
      r: UntypedRef
  ): Option[ApplicationStateMapEntry] = {
    state.getState.map.get( r )
  }

  override def persistenceId: String = id

  private object CommandHandlers {

    def handleUpdateEntityCommand( command: Update ): Unit = {
      val oldState = state.getState

      val event: events.UpdateEntityEventToBeSavedToTheJournal = {
        events.UpdateEntityEventToBeSavedToTheJournal( command )
      }

      persist( event ) { evt: events.UpdateEntityEventToBeSavedToTheJournal =>
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

      val resp: Responses.UpdateEntity_Command_Response =
        UpdateEntity_Command_Response(
          (Right( stateChange ) )
        )

      sender() ! resp

    }

    def handleInsertEntityCommand( command: Insert ): Unit = {
      val oldState = state.getState

      val event: events.CreateEntityEventToBeSavedToTheJournal = {
        events.CreateEntityEventToBeSavedToTheJournal( command )
      }

      persist( event ) { evt: events.CreateEntityEventToBeSavedToTheJournal =>
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
    case Shutdown =>
      println( "shutting down persistent actor" )
      context.stop( self )

    case command @ Insert( _ ) =>
      CommandHandlers.handleInsertEntityCommand( command )

    case command @ Update( _ ) =>
      CommandHandlers.handleUpdateEntityCommand( command )

    case GetApplicationState => {
      sender() ! GetFullApplicationState_Command_Response( state.getState )
    }

  }

  private def applyEvent( event: EventToBeSavedToTheJournal ): Unit = event match {

    case CreateEntityEventToBeSavedToTheJournal( insertNewEntityCommand ) => {
      println(
        s"\n\nApplyEvent was called with CreateEntityEvent:\n$insertNewEntityCommand"
      )

      val newEntry: ApplicationStateMapEntry = insertNewEntityCommand.newEntry

      state.unsafeInsertStateEntry( newEntry )

    }

    case UpdateEntityEventToBeSavedToTheJournal( updateEntityCommand ) => {

      println(
        s"\n\nApplyEvent was called with UpdateEntityEvent:\n$updateEntityCommand"
      )

      val newEntry: ApplicationStateMapEntry = updateEntityCommand.updatedEntry

      state.unsafeInsertUpdatedEntity( newEntry )
      // todo-one-day => handle the "unsafe-ness"

    }

  }

  override def receiveRecover: Receive = {

    case evt: EventToBeSavedToTheJournal => {
      applyEvent( evt )
    }

    case RecoveryCompleted => {
      log.info(
        "Recovery completed \n\nState is:\n" + state.getState
      )
    }

  }
}
