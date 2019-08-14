package app.server.persistence.persistentActor

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import app.server.persistence.persistentActor.Commands.{CreateEntity, GetAllState, GetStateResult}
import app.server.persistence.state.{ApplicationState, UntypedRef}
import app.shared.dataModel.value.EntityValue

import scala.language.postfixOps





object IMPersistentActor {
  def props( id: String ): Props = Props( new IMPersistentActor( id ) )
}

class IMPersistentActor( id: String )
    extends PersistentActor with ActorLogging {

  private var state : ApplicationState = getInitState

  protected def getInitState: ApplicationState = new ApplicationState()

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case Commands.ShutdownActor =>
      println( "shutting down persistent actor" )
      context.stop( self )

    case CreateEntity(e: EntityValue[_]) => {

      val untypedRef: UntypedRef = ??? //todo-next-2

      val event: Events.CreateEntityEvent =Events.CreateEntityEvent(untypedRef)

      persist(event) { evt =>
        applyEvent(evt)

      }
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

    case GetAllState => {
      import app.shared.utils.PrettyPrint
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
    case class CreateEntityEvent(entity: UntypedRef ) extends Event

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
