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

  private var applicationState: ApplicationState = getInitState

  /**
    * This is unsafe because it assumes that the key not exist
    * yet in the map.
    *
    * @param ase
    * @return
    */
  def unsafeInsertStateEntry( ase: ApplicationStateEntry ): ApplicationState = {
    assert( !applicationState.map.contains( ase.untypedRef ) ) // this can throw !!!
    val newMap = applicationState.map + (ase.untypedRef -> ase)
    ApplicationState( newMap )
  }

  def getEntity[E <: EntityValue[E]: ClassTag](
      r: UntypedRef
  ): Option[ApplicationStateEntry] = {
    this.applicationState.map.get( r )
  }

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case PersistentActorCommands.ShutdownActor =>
      println( "shutting down persistent actor" )
      context.stop( self )

    case command @ InsertNewEntityCommand( newEntry: ApplicationStateEntry ) => {

        val oldState=applicationState

        val event: events.CreateEntityEvent = {
          events.CreateEntityEvent( command )
        }

        persist( event ) { evt: events.CreateEntityEvent =>
          applyEvent( evt )
        }


      val stateChange =
        StateChange( oldState, applicationState)

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
      sender() ! GetStateResult( applicationState )
    }

  }

  private def applyEvent( event: events.Event ): Unit = event match {
    case CreateEntityEvent( insertNewEntity ) => {
      println( insertNewEntity )

      val newEntry = insertNewEntity.newEntry

      val newState = unsafeInsertStateEntry( newEntry )

      applicationState = newState
    }

  }

  override def receiveRecover: Receive = {

    case evt: events.Event => {
      applyEvent( evt )
    }

    case RecoveryCompleted => {
      log.info( "Recovery completed \n\nState is:\n" + applicationState)
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
