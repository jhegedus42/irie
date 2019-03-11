package app.server.persistence.utils

import akka.actor.{ActorSystem, Props}
import app.server.persistence.{ApplicationState, PersActorWrapper}
import app.server.persistence.persActor.IMPersistentActor
//import app.server.state.persistence.PersActorWrapper

/**
  * Created by joco on 12/10/2017.
  */
object IMPersActorWrapperFactory {

  def makePersActor(actorSystem: ActorSystem, initState:ApplicationState) = new PersActorWrapper({
    class PersActor extends IMPersistentActor("id1") {
      //        object mock extends TypedInterfaceToPersistentActorMock with MockData
      // this is how we initialize the state of the persistent actor
      override def getInitState: ApplicationState = initState
    }
     actorSystem.actorOf(Props(new PersActor()))
  })

}
