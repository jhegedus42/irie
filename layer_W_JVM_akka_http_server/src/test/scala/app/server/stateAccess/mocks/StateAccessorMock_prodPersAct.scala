package app.server.stateAccess.mocks

import app.server.persistence.ApplicationState
import app.server.persistence.utils.IMPersActorWrapperFactory
//import app.server.persistence.utils.IMPersActorWrapperFactory
import app.server.stateAccess.generalQueries.{HasPersistentActor, StateAccessorBase}

/**
  * Created by joco on 11/09/2017.
  */
trait StateAccessorMock_prodPersAct
  extends StateAccessorBase with HasPersistentActor {
   def initState: ApplicationState
  override val actor = IMPersActorWrapperFactory.makePersActor(system, initState)

  override def shutDownService() = system.terminate()
  //
}

