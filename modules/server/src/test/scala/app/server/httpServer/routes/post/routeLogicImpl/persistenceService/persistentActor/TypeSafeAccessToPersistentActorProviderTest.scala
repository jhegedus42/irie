package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.Commands.ShutdownActor
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.data.state.StateMapSnapshot
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.state.TestStateProvider
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestUsers
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration._
import io.circe.generic.auto._

class TypeSafeAccessToPersistentActorProviderTest extends FunSuite with BeforeAndAfter{

  // todo-right-now => make this test pass


  val tsap=TypeSafeAccessToPersistentActorProvider()

  implicit val ec=tsap.executionContext


  test("testGetEntityWithVersion") {


    val ae=TestUsers.aliceEntity_with_UUID0
    val aer=ae.refToEntity

//    val tsap=TypeSafeAccessToPersistentActorProvider()
    val res=tsap.getEntityWithVersion(aer)
    println(s"we ask the 'dummy' actor, and wait for the result")
    val resa: Option[Entity[User]] =Await.result(res, 1 second )
    println(s"the result is here: $resa")
    assert(ae===resa.get)

    //
    // continue-here : make this test pass with the real persistent actor
    //  using the snapshot
  }

  test("testgetSnapshot"){

    val r: StateMapSnapshot =Await.result(tsap.getSnaphot , 1 second)
    assert(TestStateProvider.getTestState===r)

    // continue-here => make this pass, this fails too
  }

//  import akka.pattern.ask
//  ask(tsap.actor,ShutdownActor)(1 second)



}
