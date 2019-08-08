package app.server.persistence

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import app.server.persistence.ApplicationState.RefValDyn
import app.server.persistence.Commands.{GetState, GetStatePAResponse, SetState}
import app.server.testData.TestData
import app.testHelpersShared.data.TestDataLabel

case class PersActorWrapper(private[this] val actor: ActorRef) {
  import akka.pattern.ask
  import akka.util.Timeout

  import scala.concurrent.duration._

  def getState: Future[GetStatePAResponse] =
    ask(actor, GetStatePACommand)(Timeout.durationToTimeout(1 seconds))
      .mapTo[GetStatePAResponse]

  def updateEntity(rfvd: RefValDyn): Future[UpdateEntityPAResponse] =
    ask(actor, UpdateEntityPACommand(rfvd))(Timeout.durationToTimeout(1 seconds))
      .mapTo[UpdateEntityPAResponse]

  def createEntity(e:Data):Future[CreateEntityPAResponse]=
    ask(actor, CreateEntityPACommand(e))(Timeout.durationToTimeout(1 seconds)).mapTo[CreateEntityPAResponse]

  def setState(s:TestDataLabel): Future[SetStatePAResponse] =
    ask(actor, SetStatePACommand(s))(Timeout.durationToTimeout(1 seconds))
      .mapTo[SetStatePAResponse]

}

object Commands {

  case class UpdateEntity(entity: RefValDyn)

  case class CreateEntity(e:RefValDyn)

  case object GetState
  case class GetStatePAResponse(state: ApplicationState)

  case class SetState(tdl:TestDataLabel)

  case object Shutdown
}

object IMPersistentActor {
  def props(id: String): Props = Props(new IMPersistentActor(id))
}

class IMPersistentActor(id: String) extends PersistentActor with ActorLogging {

  private var state: ApplicationState = getInitState

  protected def getInitState: ApplicationState = new ApplicationState()

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case Commands.Shutdown =>
      println("shutting down persistent actor")
      context.stop(self)

//    case CreateEntityPACommand(e: Entity[_]) => {
//
//      val rvd: RefValDyn = RefValDyn.makeRefValDynForNewlyCreatedEntity(e)
//
//
//      persist(CreateEntity(rvd)) { evt =>
//        applyEvent(evt)
//
//        sender() ! CreateEntityPAResponse(\/-(rvd))
//      }
//    }

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
      val s=state.toString
      import app.shared.data.utils.PrettyPrint
      val spretty=PrettyPrint.prettyPrint(s)
      println(" I am an actor and I am responding with a state : "+spretty)
      sender() ! GetStatePAResponse(state)
    }

    case SetState(tdl:TestDataLabel) => {
      val ns=TestData.getTestDataFromLabels(tdl)
    }

  }

  override def receiveRecover: Receive = {

    case evt: Events.Event => {
      applyEvent(evt)
    }


    case RecoveryCompleted => {
      log.info("Recovery completed!" + state)
    }



  }

  object Events {
    //events
    sealed trait Event
    case class UpdateEntity(entity: RefValDyn) extends Event
    case class CreateEntity(entity: RefValDyn) extends Event

  }

  private def applyEvent(event: Events.Event): Unit = event match {

//    case UpdateEntity(refVal: (RefValDyn)) => {
//      val res = state.updateEntity(refVal)
//      if (res.isRight) { state = res.toEither.right.get._1 } else {
//        log.error(s" apply Event - UpdateEntity - $refVal - ($res.toString)")
//      }
//
//    }


    case Events.CreateEntity(refVal: (RefValDyn)) => {
      state = state.insertEntity(refVal)
      }

  }

}
