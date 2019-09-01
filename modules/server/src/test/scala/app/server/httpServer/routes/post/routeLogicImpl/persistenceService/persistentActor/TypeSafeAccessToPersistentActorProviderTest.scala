package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor

import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.initialization.testing.TestUsers
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration._
import io.circe.generic.auto._

class TypeSafeAccessToPersistentActorProviderTest extends FunSuite {

  // todo-right-now => make this test pass

  test("testGetEntityWithVersion") {


    val ae=TestUsers.aliceEntity_with_UUID0
    val aer=ae.refToEntity
    val tsap=TypeSafeAccessToPersistentActorProvider()
    val res=tsap.getEntityWithVersion(aer)
    val resa: Option[Entity[User]] =Await.result(res, 1 second )
    assert(ae===resa.get)
  }

  test("test getSnaphot"){
    // continue-here : implement this test, and make it pass, with some test
    //  data
  }

}
