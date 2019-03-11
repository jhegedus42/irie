package app.server.stateAccess.tests

import akka.actor.ActorSystem
import app.server.persistence.ApplicationState
import app.server.stateAccess.generalQueries.InterfaceToStateAccessor
import app.server.stateAccess.mocks.StateAccessorMock_prodPersAct
import app.shared.SomeError_Trait
import app.shared.data.model.Entity.Data
import app.shared.data.ref.{Ref, RefVal}

import scala.concurrent.Future
import scalaz.\/

/**
  * Created by joco on 10/09/2017.
  */
// this one using real persistence service
class EntityServiceTest extends EntityServiceTest_BaseTrait {
  override def getEntityService(s:ApplicationState ): InterfaceToStateAccessor =
    new StateAccessorMock_prodPersAct {
      override implicit lazy val system: ActorSystem = ActorSystem("EntityService_Test_ProdPers-getEntityService")

      override def initState: ApplicationState = s
      override def shutDownService()= system.terminate()

    }

}
