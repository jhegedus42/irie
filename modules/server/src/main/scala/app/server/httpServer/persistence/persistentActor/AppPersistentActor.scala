package app.server.httpServer.persistence.persistentActor

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import app.server.httpServer.persistence.persistentActor.PersistentActorCommands.{
  GetAllStateCommand,
  InsertNewEntityCommand
}
import app.server.httpServer.persistence.persistentActor.Responses.GetStateResult
import app.server.httpServer.persistence.persistentActor.events.CreateEntityEvent
import app.server.httpServer.persistence.persistentActor.state.{
  ApplicationState,
  ApplicationStateEntry,
  UntypedRef
}
import app.shared.dataModel.value.EntityValue

import scala.language.postfixOps
import scala.reflect.ClassTag

object AppPersistentActor {
  val as: ActorSystem = ActorSystem()

  def getActor( id: String ) = as.actorOf( props( id ) )

  def props( id: String ): Props = Props( new AppPersistentActor( id ) )
}


private[persistentActor] class AppPersistentActor( id: String )
    extends PersistentActor with ActorLogging {

  private object ApplicationStateWrapper{
    private var applicationState: ApplicationState = getInitState
    def getState = applicationState
    def setState(s:ApplicationState) :Unit=  {
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
  def unsafeInsertStateEntry( ase: ApplicationStateEntry ): ApplicationState = {
    assert( !ApplicationStateWrapper.getState.map.contains( ase.untypedRef ) ) // this can throw !!!
    val newMap = ApplicationStateWrapper.getState.map + (ase.untypedRef -> ase)
    ApplicationState( newMap )
  }

  def getEntity[E <: EntityValue[E]: ClassTag](
      r: UntypedRef
  ): Option[ApplicationStateEntry] = {
    ApplicationStateWrapper.getState.map.get( r )
  }

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case PersistentActorCommands.ShutdownActor =>
      println( "shutting down persistent actor" )
      context.stop( self )

    case command @ InsertNewEntityCommand( newEntry: ApplicationStateEntry ) => {

        val oldState=ApplicationStateWrapper.getState

        val event: events.CreateEntityEvent = {
          events.CreateEntityEvent( command )
        }


        persist( event ) { evt: events.CreateEntityEvent =>
          applyEvent( evt )
        }


      val stateChange = StateChange( oldState, ApplicationStateWrapper.getState)

      println("ReceiveCommand in case InsertNewEntityCommand, size of maps in StateChange:\n")
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
      println( insertNewEntity )

      val newEntry = insertNewEntity.newEntry

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

  protected def getInitState: ApplicationState = new ApplicationState()
  // match {

  ////    case UpdateEntity(refVal: (RefValDyn)) => {
  ////      state = state.updateEntity(refVal)
  ////    }
  //
  //    case events.CreateEntityEvent( untypedRef: (UntypedRef) ) => {
  ////      state = state.insertEntity(refVal)
  //      // todo-next-3
  //    }
  //
  //  }

}
