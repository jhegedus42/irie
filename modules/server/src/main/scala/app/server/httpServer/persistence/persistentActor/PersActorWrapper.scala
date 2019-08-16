package app.server.httpServer.persistence.persistentActor

import akka.actor.ActorRef
import app.server.httpServer.persistence.persistentActor.PersistentActorCommands.{
  GetAllStateCommand,
  InsertNewEntityCommand
}
import app.server.httpServer.persistence.persistentActor.Responses.InsertNewEntityCommandResponse
import app.server.httpServer.persistence.state.{ ApplicationStateEntry, ApplicationStateMap }
import app.shared.dataModel.value.EntityValue
import app.shared.dataModel.value.refs.Entity

import scala.concurrent.{ExecutionContextExecutor, Future}

case class StateChange(
    before: ApplicationStateMap,
    after:  Option[ApplicationStateMap]
)

private[persistence] case class PersActorWrapper( val actor: ActorRef ) {

  import akka.pattern.ask
  import akka.util.Timeout

  import scala.concurrent.duration._

  def getState: Future[Responses.GetStateResult] =
    ask( actor, GetAllStateCommand )( Timeout.durationToTimeout( 1 seconds ) )
      .mapTo[Responses.GetStateResult]

  //  def updateEntity(rfvd: RefValDyn): Future[UpdateEntityPAResponse] =
  //    ask(actor, UpdateEntityPACommand(rfvd))(Timeout.durationToTimeout(1 seconds))
  //      .mapTo[UpdateEntityPAResponse]

  /**
    * Inserts an entity into the Map containeing the Appliacation State.
    *
    * @param entityToInsert Entity to insert. (This should not exist yet in the persistent actor).
    *                       It has to have version number 0.
    * @tparam E value contained in the entity.
    * @return the application state before and after the insertion :
    *         (in a StateChange object, first element is the state before,
    *         second one is the state after the insertion).
    */
  def insertEntity[E <: EntityValue[E]](
      entityToInsert: Entity[E]
  )( implicit executionContextExecutor: ExecutionContextExecutor ): Future[StateChange] = {
    // todo-later we should check that the entity's version number is zero, i.e.
    //  we are dealing with an entity which has not been inserted into the
    //  ApplicationStateMap

    val newEntry: ApplicationStateEntry = ???
    // todo-now-4 insertEntity in PersActorWrapper
    //  convert entity `E` into `ApplicationStateEntry`

    val res: Future[InsertNewEntityCommandResponse] =
      ask( actor, InsertNewEntityCommand( newEntry ) )(
        Timeout.durationToTimeout( 1 seconds )
      ).mapTo[InsertNewEntityCommandResponse]

    res.map( x => x.stateChange )
  }

}
