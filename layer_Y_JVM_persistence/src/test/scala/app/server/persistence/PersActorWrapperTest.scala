package app.server.persistence

import akka.actor.ActorSystem
import app.server.persistence.persActor.Commands.GetStatePAResponse
import app.server.persistence.utils.IMPersActorWrapperFactory
//import app.server.state.persistence.utils.IMPersActorWrapperFactory
import app.testHelpersServer.state.TestData
import app.testHelpersShared.data.TestDataLabels.{LabelOne, LabelTwo}
import app.testHelpersShared.implicits.ForTestingOnly
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by joco on 11/10/2017.
  */
class PersActorWrapperTest extends FunSuite {

  test("testGetState") {

    val state = TestData.getTestDataFromLabels(LabelOne)
    val state2 = TestData.getTestDataFromLabels(LabelTwo)
    implicit lazy val system: ActorSystem = ActorSystem("PersActorWrapperTest-testGetState")

    val pa: PersActorWrapper =
      IMPersActorWrapperFactory.makePersActor(system, state)

    val res: GetStatePAResponse = Await.result(pa.getState, 2 seconds)
    val stateres = res.state
    assert(state == stateres)
    assert(stateres != state2)
    // https://docs.scala-lang.org/overviews/collections/equality.html
    system.terminate()
  }

  test("testSetState") {
//                         35e98eb314ad4f029294b9f7094bcce6
    val state = TestData.getTestDataFromLabels(LabelOne)
    val state2 = TestData.getTestDataFromLabels(LabelTwo)
    implicit lazy val system: ActorSystem = ActorSystem("PersActorWrapperTest-testSetState")

    val pa: PersActorWrapper =
      IMPersActorWrapperFactory.makePersActor(system, state)

    val res: GetStatePAResponse = Await.result(pa.getState, 2 seconds)
    val stateres = res.state
    assert(state == stateres)
    assert(stateres != state2)

    pa.setState(LabelTwo)

    val res2: GetStatePAResponse = Await.result(pa.getState, 2 seconds)
    val stateres2 = res2.state
    assert(state2 == stateres2)
    assert(stateres2 != state)
    system.terminate()
  }

}
