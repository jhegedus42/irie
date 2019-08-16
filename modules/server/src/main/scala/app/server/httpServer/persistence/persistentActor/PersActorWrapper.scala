package app.server.httpServer.persistence.persistentActor

import akka.actor.ActorRef
import app.server.httpServer.persistence.persistentActor.PersistentActorCommands.{
  GetAllState,
  GetStateResult,
  InsertNewEntity
}
import app.server.httpServer.persistence.state.{
  ApplicationStateEntry,
  ApplicationStateMap
}
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.Entity

import scala.concurrent.Future

case class StateChange(
    before: ApplicationStateMap,
    after:  ApplicationStateMap
)

private[persistence] case class PersActorWrapper( val actor: ActorRef ) {
  import akka.pattern.ask
  import akka.util.Timeout

  import scala.concurrent.duration._

  def getState: Future[GetStateResult] =
    ask( actor, GetAllState )( Timeout.durationToTimeout( 1 seconds ) )
      .mapTo[GetStateResult]

//  def updateEntity(rfvd: RefValDyn): Future[UpdateEntityPAResponse] =
//    ask(actor, UpdateEntityPACommand(rfvd))(Timeout.durationToTimeout(1 seconds))
//      .mapTo[UpdateEntityPAResponse]

  /**
    * Inserts an entity into the Map containeing the Appliacation State.
    *
    * @param entityToInsert Entity to insert. (This should not exist yet in the persistent actor).
    *          It has to have version number 0.
    * @tparam E value contained in the entity.
    * @return the application state before and after the insertion :
    *         (in a StateChange object, first element is the state before,
    *         second one is the state after the insertion).
    */
  def insertEntity[E <: EntityValue[E]](
      entityToInsert: Entity[E]
  ): Future[StateChange] = {
    // todo-later we should check that the entity's version number is zero, i.e.
    //  we are dealing with an entity which has not been inserted into the
    //  ApplicationStateMap

    val newEntry: ApplicationStateEntry = ??? // todo-now
    // todo-now
    //  convert entity `E` into `ApplicationStateEntry`

    ask( actor, InsertNewEntity( newEntry ) )(
      Timeout.durationToTimeout( 1 seconds )
    ).mapTo[StateChange]
  }

}
