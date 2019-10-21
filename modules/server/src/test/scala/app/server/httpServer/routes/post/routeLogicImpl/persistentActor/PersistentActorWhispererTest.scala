package app.server.httpServer.routes.post.routeLogicImpl.persistentActor

import akka.actor.ActorSystem
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.data.Commands.ShutdownActor
import app.server.httpServer.routes.post.routeLogicImpl.persistentActor.state.TestDataProvider
import app.shared.entity.EntityWithRef
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestEntitiesForUsers
import app.shared.state.StateMapSnapshot
import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.concurrent.duration._
import io.circe.generic.auto._

class PersistentActorWhispererTest extends FunSuite with BeforeAndAfter{

    val as : ActorSystem = ActorSystem()

  implicit val ec=as.dispatcher
  val tsap=PersistentActorWhisperer(as)



  test("testGetEntityWithVersion") {


    val ae=TestEntitiesForUsers.aliceEntity_with_UUID0
    val aer=ae.refToEntity

    val res=tsap.getEntityWithVersion(aer)
    val resa: Option[EntityWithRef[User]] =Await.result(res, 1 second )
    println(s"the result is here: $resa")
    assert(ae===resa.get)

  }

//  test("testgetSnapshot"){
//
//    val r: StateMapSnapshot =Await.result(tsap.getSnaphot , 1 second)
//    assert(TestDataProvider.getTestState===r)
//
//  }




}
