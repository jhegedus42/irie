package app.server.httpServer.persistence.persistentActor

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import app.server.httpServer.persistence.persistentActor.PersistentActorCommands.{GetAllStateCommand, InsertNewEntityCommand}
import app.server.httpServer.persistence.persistentActor.Responses.GetStateResult
import app.server.httpServer.persistence.state.{ApplicationStateEntry, ApplicationStateMap, UntypedRef}
import app.shared.dataModel.value.EntityValue

import scala.language.postfixOps





object AppPersistentActor {
  def props( id: String ): Props = Props( new AppPersistentActor( id ) )
  val as: ActorSystem =ActorSystem()
  def getActor(id:String) = as.actorOf(props(id))
}

private[persistentActor] class AppPersistentActor(id: String )
    extends PersistentActor with ActorLogging {

  private var state : ApplicationStateMap = getInitState

  protected def getInitState: ApplicationStateMap = new ApplicationStateMap()

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case PersistentActorCommands.ShutdownActor =>
      println( "shutting down persistent actor" )
      context.stop( self )

    case InsertNewEntityCommand(newEntry:ApplicationStateEntry ) => {

      val oldApplicationStateMap: ApplicationStateMap = state
      val newApplicationStateMap = oldApplicationStateMap.insertNewApplicationStateEntry(
        newEntry
      )

      if(!newApplicationStateMap.isEmpty) state=newApplicationStateMap.get

      val event: Events.CreateEntityEvent = ???
        // todo-now-5 fix this
        //  create a replayable event
        //   => wrap the InsertNewEntity command
        //      into an event

      persist(event) { evt: Events.CreateEntityEvent =>
        applyEvent(evt)
      }

      val stateChange= StateChange(oldApplicationStateMap,newApplicationStateMap)

      sender() ! Responses.InsertNewEntityCommandResponse ( stateChange )

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
      import app.server.utils.PrettyPrint
      val s       = state.toString
      val spretty = PrettyPrint.prettyPrint( s )
      println( " I am an actor and I am responding with a state : " + spretty )
      sender() ! GetStateResult( state )
    }


  }

  override def receiveRecover: Receive = {

    case evt: Events.Event => {
      applyEvent( evt )
    }

    case RecoveryCompleted => {
      log.info( "Recovery completed!" + state )
    }

  }

  object Events {
    //events
    sealed trait Event
//    case class UpdateEntityEvent[E <: Entity[E]](entity: UntypedRef )
//        extends Event
    case class CreateEntityEvent(insertNewEntity: InsertNewEntityCommand) extends Event

  }

  private def applyEvent( event: Events.Event ): Unit = event match {

//    case UpdateEntity(refVal: (RefValDyn)) => {
//      state = state.updateEntity(refVal)
//    }

    case Events.CreateEntityEvent( untypedRef: (UntypedRef) ) => {
//      state = state.insertEntity(refVal)
      // todo-next-3
    }

  }

}
