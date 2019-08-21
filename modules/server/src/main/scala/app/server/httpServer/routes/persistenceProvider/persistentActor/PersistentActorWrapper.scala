package app.server.httpServer.routes.persistenceProvider.persistentActor

import akka.actor.ActorRef
import app.server.httpServer.routes.persistenceProvider.persistentActor.Responses.InsertNewEntityCommandResponse
import app.server.httpServer.routes.persistenceProvider.persistentActor.commands.{GetAllStateCommand, InsertNewEntityCommand}
import app.shared.entity.Entity
import app.shared.entity.asString.EntityAsString
import app.shared.entity.entityValue.EntityValue
import app.shared.state.{ApplicationStateMapEntry, StateChange, UntypedRef}
import io.circe.Encoder

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.postfixOps

private[persistenceProvider] case class PersistentActorWrapper(val actor: ActorRef) {

  import akka.pattern.ask
  import akka.util.Timeout

  import scala.concurrent.duration._

  def getState: Future[Responses.GetStateResult] =
    ask(actor, GetAllStateCommand)(Timeout.durationToTimeout(1 seconds))
      .mapTo[Responses.GetStateResult]

  //  def updateEntity(rfvd: RefValDyn): Future[UpdateEntityPAResponse] =
  //    ask(actor, UpdateEntityPACommand(rfvd))(Timeout.durationToTimeout(1 seconds))
  //      .mapTo[UpdateEntityPAResponse]

  /**
    * Inserts an entity into the Map containeing the Appliacation State.
    *
    * @param entityToInsert Entity to insert. (This should not exist yet in the persistent actor).
    *                       It has to have version number 0.
    * @tparam V value contained in the entity.
    * @return the application state before and after the insertion :
    *         (in a StateChange object, first element is the state before,
    *         second one is the state after the insertion).
    */
  def insertEntity[V <: EntityValue[V]](entityToInsert: Entity[V])(
    implicit executionContextExecutor: ExecutionContextExecutor,
    encoder: Encoder[Entity[V]]
  ): Future[StateChange] = {

    // todo-later
    //  we should check that the entity's version number is zero, i.e.
    //  we are dealing with an entity which has not been inserted into the
    //  ApplicationStateMap

    val newEntry: ApplicationStateMapEntry = {
      val utr: UntypedRef =
        UntypedRef.makeFromRefToEntity[V](entityToInsert.refToEntity)

      val entityAsString: EntityAsString = entityToInsert.entityAsString()

      val applicationStateEntry =
        ApplicationStateMapEntry(utr, entityAsString)
      applicationStateEntry
    }

    val res: Future[InsertNewEntityCommandResponse] =
      ask(actor, InsertNewEntityCommand(newEntry))(
        Timeout.durationToTimeout(1 seconds)
      ).mapTo[InsertNewEntityCommandResponse]

    res.map(x => x.stateChange)

  }

}
