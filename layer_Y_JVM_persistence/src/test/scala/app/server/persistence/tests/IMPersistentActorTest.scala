package app.server.persistence.tests

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import app.server.persistence.ApplicationState
import app.server.persistence.persActor.Commands.{CreateEntityPACommand, CreateEntityPAResponse, GetStatePACommand, GetStatePAResponse}
import app.server.persistence.persActor.IMPersistentActor
import app.shared.SomeError_Trait
import app.shared.data.model.LineText
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scalaz.{\/, \/-}

/**
  * Created by joco on 02/05/2017.
  */
class IMPersistentActorTest
    extends TestKit(ActorSystem("IMPersistentActorTest"))
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with ImplicitSender {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Actor" should {
    val el = LineText(title = "bla",text="testText")

    "create an entity and preserve it after restart" in {
      import app.server.persistence.persActor.Shutdown
      //            val testActor = system.actorOf(Props(new IMPersistentActor("sc-000001") with RestartableActor))
      class TestActor extends IMPersistentActor("id1")
      val testActor: ActorRef = system.actorOf(Props(new TestActor()))

//      testActor ! AddElementCommand(DynEntity.refVal2DynamicEntity(refVal))
      testActor ! CreateEntityPACommand(el)

      import scala.concurrent.duration._
      val cc: Class[_ <: CreateEntityPAResponse] = CreateEntityPAResponse(null).getClass()

      val cer: CreateEntityPAResponse = expectMsgClass(max = 2 seconds, c= cc)
      val CreateEntityPAResponse(\/-(refValDyn))= cer

      testActor ! GetStatePACommand


//      val rvd =

      val stateV: \/[SomeError_Trait, ApplicationState] = ApplicationState().insertEntity(refValDyn)
      val state=stateV.toEither.right.get

      expectMsg(GetStatePAResponse(state))
      //      testActor ! RestartActor
      testActor ! Shutdown
//      testActor ! PoisonPill

      val testActor2: ActorRef =
        system.actorOf((IMPersistentActor.props("id1")))

      testActor2 ! GetStatePACommand

      expectMsg(GetStatePAResponse(state))
    }
  }
//    "add an item to the list and preserve it after restart" in {
//      //      val testActor = system.actorOf(Props(new IMPersistentActor("sc-000001") with RestartableActor))
//      val id="id2"
//      val testActor = system.actorOf((IMPersistentActor.props(id)))
//
//      testActor ! AddElementCommand(refVal)
//      expectMsg(AddElementResponse(refVal))
//
//      testActor ! RemoveElementCommand(refVal.id)
//
////      testActor.mySend(RemoveElementCommand(refVal.id),testActor)
//
//      //      testActor ! RestartActor
//      testActor ! PoisonPill
//
//      val testActor2 = system.actorOf((IMPersistentActor.props(id)))
//
//      testActor2 ! GetElementsRequest
//
//      expectMsg(GetElementsResponse(Seq(refVal)))
//    }

//  }



}
