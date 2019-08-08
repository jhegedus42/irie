package app.server.httpServer

//import app.server.persistence.utils.IMPersActorWrapperFactory

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

