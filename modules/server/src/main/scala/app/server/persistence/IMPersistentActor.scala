package app.server.persistence

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import app.server.persistence.ApplicationState.RefValDyn
import app.server.persistence.Commands.{CreateEntity, GetState, GetStateResult, SetState, UpdateEntity}
import app.server.testData.TestData
import app.shared.dataModel.entity.Entity.Entity
import app.shared.dataModel.entity.refs.TypedRefVal
import app.shared.dataModel.entity.testData.TestDataLabel

import scala.concurrent.Future

case class PersActorWrapper( val actor: ActorRef ) {
  import akka.pattern.ask
  import akka.util.Timeout

  import scala.concurrent.duration._

  def getState: Future[GetStateResult] =
    ask( actor, GetState )( Timeout.durationToTimeout( 1 seconds ) )
      .mapTo[GetStateResult]

//  def updateEntity(rfvd: RefValDyn): Future[UpdateEntityPAResponse] =
//    ask(actor, UpdateEntityPACommand(rfvd))(Timeout.durationToTimeout(1 seconds))
//      .mapTo[UpdateEntityPAResponse]

//  def createEntity(e:Data):Future[CreateEntityPAResponse]=
//    ask(actor, CreateEntityPACommand(e))(Timeout.durationToTimeout(1 seconds)).mapTo[CreateEntityPAResponse]

  def setState( s: TestDataLabel ): Unit =
    ask( actor, SetState( s ) )( Timeout.durationToTimeout( 1 seconds ) )

}

object Commands {

  case class UpdateEntity[E <: Entity[E]]( entity: TypedRefVal[E] )

  case class CreateEntity[E <: Entity[E]]( e: TypedRefVal[E] )

  case object GetState
  case class GetStateResult( state: ApplicationState )

  case class SetState( tdl: TestDataLabel )

  case object Shutdown
}

object IMPersistentActor {
  def props( id: String ): Props = Props( new IMPersistentActor( id ) )
}

class IMPersistentActor( id: String )
    extends PersistentActor with ActorLogging {

  private var state: ApplicationState = getInitState

  protected def getInitState: ApplicationState = new ApplicationState()

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case Commands.Shutdown =>
      println( "shutting down persistent actor" )
      context.stop( self )

    case CreateEntity(e: Entity[_]) => {

//      val rvd: RefValDyn = RefValDyn.makeRefValDynForNewlyCreatedEntity(e)
      val rvd: RefValDyn = ??? //todo fix this


      persist(Events.CreateEntityEvent(rvd)) { evt =>
        applyEvent(evt)

//        sender() ! CreateEntityPAResponse(\/-(rvd))
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

    case GetState => {
      import app.shared.utils.PrettyPrint
      val s       = state.toString
      val spretty = PrettyPrint.prettyPrint( s )
      println( " I am an actor and I am responding with a state : " + spretty )
      sender() ! GetStateResult( state )
    }

    case SetState( tdl: TestDataLabel ) => {
      val ns = TestData.getTestDataFromLabels( tdl )
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
    case class UpdateEntityEvent[E <: Entity[E]](entity: TypedRefVal[E] )
        extends Event
    case class CreateEntityEvent[T <: Entity[T]](entity: TypedRefVal[T] )
        extends Event

  }

  private def applyEvent( event: Events.Event ): Unit = event match {

//    case UpdateEntity(refVal: (RefValDyn)) => {
//      state = state.updateEntity(refVal)
//    }

    case Events.CreateEntityEvent( refVal: (RefValDyn) ) => {
//      state = state.insertEntity(refVal)
      // todo ^^^ implement this uncommented line
    }

  }

}
