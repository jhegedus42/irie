package app.server.stateAccess.generalQueries

import akka.actor.ActorSystem
import app.server.persistence.PersActorWrapperIF

/**
  * Created by joco on 17/10/2017.
  */
trait HasPersistentActor {

  val actor: PersActorWrapperIF

  implicit val system: ActorSystem

}
