package app.server.httpServer.routes.post.routeLogicImpl.persistentActor

import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.Commands.ShutdownActor
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.state.StateMapSnapshot
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.state.TestDataProvider
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestUsers
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration._
import io.circe.generic.auto._

class PersistentActorWhispererTest extends FunSuite with BeforeAndAfter{


  val tsap=PersistentActorWhisperer()

  implicit val ec=tsap.executionContext


  test("testGetEntityWithVersion") {


    val ae=TestUsers.aliceEntity_with_UUID0
    val aer=ae.refToEntity

    val res=tsap.getEntityWithVersion(aer)
    val resa: Option[Entity[User]] =Await.result(res, 1 second )
    println(s"the result is here: $resa")
    assert(ae===resa.get)

  }

  test("testgetSnapshot"){

    val r: StateMapSnapshot =Await.result(tsap.getSnaphot , 1 second)
    assert(TestDataProvider.getTestState===r)

  }




}
