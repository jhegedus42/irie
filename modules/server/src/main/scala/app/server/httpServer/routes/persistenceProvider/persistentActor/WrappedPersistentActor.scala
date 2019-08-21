package app.server.httpServer.routes.persistenceProvider.persistentActor

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import app.server.httpServer.routes.persistenceProvider.persistentActor.PersistentActorCommands.{GetAllStateCommand, InsertNewEntityCommand}
import app.server.httpServer.routes.persistenceProvider.persistentActor.Responses.GetStateResult
import app.server.httpServer.routes.persistenceProvider.persistentActor.events.CreateEntityEvent
import app.shared.state.{ApplicationStateMap, ApplicationStateMapEntry, StateChange, StatePrintingUtils, UntypedRef}
import app.shared.entity.entityValue.EntityValue
import app.shared.initialization.Config

import scala.language.postfixOps
import scala.reflect.ClassTag

object WrappedPersistentActor {
  val as: ActorSystem = ActorSystem()

  def getActor( id: String ) = as.actorOf( props( id ) )

  def props( id: String ): Props = Props( new WrappedPersistentActor( id ) )
}


private[persistentActor] class WrappedPersistentActor(id: String )
    extends PersistentActor with ActorLogging {

  private object ApplicationStateWrapper{

    val areWeTesting= Config.details.areWeTesting
    val testState=Config.details.testApplicationState.applicationStateMap
    private var applicationState: ApplicationStateMap = if(!areWeTesting)
      new ApplicationStateMap()
    else testState


    def getState = applicationState
    def setState(s:ApplicationStateMap) :Unit=  {
      println("\n\nState was set to:\n")
      StatePrintingUtils.printApplicationState(s)
      applicationState=s
    }

  }


  /**
    * This is unsafe because it assumes that the key not exist
    * yet in the map.
    *
    * @param ase
    * @return
    */
  def unsafeInsertStateEntry( ase: ApplicationStateMapEntry ): ApplicationStateMap = {
    assert( !ApplicationStateWrapper.getState.map.contains( ase.untypedRef ) ) // this can throw !!!
    val newMap = ApplicationStateWrapper.getState.map + (ase.untypedRef -> ase)
    ApplicationStateMap( newMap )
  }

  def getEntity[V <: EntityValue[V]: ClassTag](
      r: UntypedRef
  ): Option[ApplicationStateMapEntry] = {
    ApplicationStateWrapper.getState.map.get( r )
  }

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case PersistentActorCommands.ShutdownActor =>
      println( "shutting down persistent actor" )
      context.stop( self )

    case command @ InsertNewEntityCommand( newEntry: ApplicationStateMapEntry ) => {

        val oldState=ApplicationStateWrapper.getState

        val event: events.CreateEntityEvent = {
          events.CreateEntityEvent( command )
        }


        persist( event ) { evt: events.CreateEntityEvent =>
          applyEvent( evt )
        }


      val stateChange = StateChange( oldState, ApplicationStateWrapper.getState)

      println(
          "\n\n" +
          "ReceiveCommand was called\n" +
          "and matched the case 'InsertNewEntityCommand',\n" +
          "size of maps in StateChange:\n")
      println(stateChange.getSizeOfMapsBeforeAndAfter)
      println()

      sender() ! Responses.InsertNewEntityCommandResponse( stateChange )

    }

    //    case UpdateEntityPACommand(item) => {
    //
    //      val res: \/[SomeError_Trait, (ApplicationState, RefValDyn)] =
    //        state.updateEntity(item)
    //      if (res.isRight) {
    //        persist(UpdateEntity(item)) { evt =>
    //          applyEvent(evt)
    //          val rp = UpdateEntityPAResponse(\/-(res.toEither.right.get._2))
    //          println(rp)
    //          sender() ! rp
    //        }
    //      } else {
    //        sender() ! UpdateEntityPAResponse(-\/(res.toEither.left.get))
    //      }
    //    }

    case GetAllStateCommand => {
      sender() ! GetStateResult( ApplicationStateWrapper.getState)
    }

  }

  private def applyEvent( event: events.Event ): Unit = event match {
    case CreateEntityEvent( insertNewEntity ) => {
      println( s"\n\nApplyEvent was called with CreateEntityEvent:\n$insertNewEntity")

      val newEntry: ApplicationStateMapEntry = insertNewEntity.newEntry

      val newState = unsafeInsertStateEntry( newEntry )

      ApplicationStateWrapper.setState(newState)
    }

  }

  override def receiveRecover: Receive = {

    case evt: events.Event => {
      applyEvent( evt )
    }

    case RecoveryCompleted => {
      log.info( "Recovery completed \n\nState is:\n" + ApplicationStateWrapper.getState)
    }

  }

}
