package app.server.persistence.persActor

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted}
import Commands.{CreateEntityPACommand, CreateEntityPAResponse, GetStatePACommand, GetStatePAResponse, SetStatePACommand, UpdateEntityPACommand, UpdateEntityPAResponse}
import EventsStoredInJournal.{CreateEntity, Event, UpdateEntity}
import app.server.persistence.ApplicationState
import app.shared.SomeError_Trait
import app.shared.data.model.Entity.{Data, Entity}
import app.shared.data.ref.RefValDyn
import app.shared.data.utils.PrettyPrint
import app.testHelpersServer.state.TestData
import app.testHelpersShared.data.TestDataLabels.TestDataLabel
import app.testHelpersShared.implicits.ForTestingOnly
import scalaz.{-\/, \/, \/-}

case object Shutdown

object EventsStoredInJournal {
  //events
  sealed trait Event
  case class UpdateEntity(entity: RefValDyn) extends Event
  case class CreateEntity(entity: RefValDyn) extends Event

}

object IMPersistentActor {
  def props(id: String): Props = Props(new IMPersistentActor(id))
}

class IMPersistentActor(id: String) extends PersistentActor with ActorLogging {

  private var state: ApplicationState = getInitState

  protected def getInitState: ApplicationState = new ApplicationState()


//  import app.server.persistence
  // we need this so that we can override this for testing purposes (to set the initial state)

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case Shutdown =>
      println("shutting down persistent actor")
      context.stop(self)

    case CreateEntityPACommand(e: Entity) => {

      val rvd: RefValDyn = RefValDyn.makeRefValDynForNewlyCreatedEntity(e)


      persist(CreateEntity(rvd)) { evt =>
        applyEvent(evt)

        sender() ! CreateEntityPAResponse(\/-(rvd))
      }
    }

    case UpdateEntityPACommand(item) => {

      val res: \/[SomeError_Trait, (ApplicationState, RefValDyn)] =
        state.updateEntity(item)
      if (res.isRight) {
        persist(UpdateEntity(item)) { evt =>
          applyEvent(evt)
          val rp = UpdateEntityPAResponse(\/-(res.toEither.right.get._2))
          println(rp)
          sender() ! rp
        }
      } else {
        sender() ! UpdateEntityPAResponse(-\/(res.toEither.left.get))
      }
    }
    case GetStatePACommand => {
      val s=state.toString
      import app.shared.data.utils.PrettyPrint
      val spretty=PrettyPrint.prettyPrint(s)
      println(" I am an actor and I am responding with a state : "+spretty)
      sender() ! GetStatePAResponse(state)
    }

    case SetStatePACommand(tdl:TestDataLabel) => {
      val ns=TestData.getTestDataFromLabels(tdl)
      if (ns!=null) state = ns else
        println("we try to set the state to null, that is fucked up")
      println("new state is:\n"+PrettyPrint.prettyPrint(ns))
    }

  }

  override def receiveRecover: Receive = {

    case evt: Event => {
      applyEvent(evt)
    }


    case RecoveryCompleted => {
      log.info("Recovery completed!" + state)
    }



  }

  private def applyEvent(event: Event): Unit = event match {
    case UpdateEntity(refVal: (RefValDyn)) => {
      val res = state.updateEntity(refVal)
      if (res.isRight) { state = res.toEither.right.get._1 } else {
        log.error(s" apply Event - UpdateEntity - $refVal - ($res.toString)")
      }

    }

    case CreateEntity(refVal: (RefValDyn)) => {
      val res = state.insertEntity(refVal)
      if (res.isRight) { state = res.toEither.right.get } else {
        log.error(s" apply Event - InsertEntity - $refVal - ($res.toString)")
      }
    }
  }

}
