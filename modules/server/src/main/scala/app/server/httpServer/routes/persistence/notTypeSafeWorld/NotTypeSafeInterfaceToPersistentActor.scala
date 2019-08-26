package app.server.httpServer.routes.persistence.notTypeSafeWorld

import akka.actor.ActorRef
import app.server.httpServer.routes.persistence.notTypeSafeWorld.persistentActor.Responses.{
  InsertNewEntity_Command_Response,
  UpdateEntity_Command_Response
}
import app.server.httpServer.routes.persistence.notTypeSafeWorld.persistentActor.commands.{
  GetFullApplicationState_Command,
  InsertNewEntity_Command,
  UpdateEntity_Command
}
import app.server.httpServer.routes.persistence.notTypeSafeWorld.state.UntypedRef
import app.server.httpServer.routes.persistence.notTypeSafeWorld.persistentActor.{
  Errors,
  PersistentActorForOurApp,
  Responses
}
import app.server.utils.PrettyPrint
import app.shared.entity.Entity
import app.shared.entity.asString.EntityAsString
import app.shared.entity.entityValue.EntityValue
import io.circe.Encoder

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.postfixOps

private[persistence] case class NotTypeSafeInterfaceToPersistentActor(
//    context: ExecutionContextExecutor
) {

  import akka.pattern.ask
  import akka.util.Timeout

  import scala.concurrent.duration._
//  implicit val context_as_implicit = context

  val actor: ActorRef =
    PersistentActorForOurApp.getActor( "the_one_and_only_parsistent_actor" )

  def getState: Future[Responses.GetFullApplicationState_Command_Response] =
    ask( actor, GetFullApplicationState_Command )(
      Timeout.durationToTimeout( 1 seconds )
    ).mapTo[Responses.GetFullApplicationState_Command_Response]

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
  def insertEntity[V <: EntityValue[V]]( entityToInsert: Entity[V] )(
      implicit executionContextExecutor:                 ExecutionContextExecutor,
      encoder:                                           Encoder[Entity[V]]
  ): Future[StateChange] = {

    assert( entityToInsert.refToEntity.entityVersion.versionNumberLong == 0 )

    val newEntry: ApplicationStateMapEntry = {
      val utr: UntypedRef =
        UntypedRef.makeFromRefToEntity[V]( entityToInsert.refToEntity )

      val entityAsString: EntityAsString = entityToInsert.entityAsString()

      val applicationStateEntry =
        ApplicationStateMapEntry( utr, entityAsString )
      applicationStateEntry
    }

    val res: Future[InsertNewEntity_Command_Response] =
      ask( actor, InsertNewEntity_Command( newEntry ) )(
        Timeout.durationToTimeout( 1 seconds )
      ).mapTo[InsertNewEntity_Command_Response]

    res.map( x => x.stateChange )

  }

  def updateEntity[V <: EntityValue[V]]( entityToUpdate: Entity[V] )(
      implicit executionContextExecutor:                 ExecutionContextExecutor,
      encoder:                                           Encoder[Entity[V]]
  ): Future[Either[Errors.EntityUpdateVersionError, StateChange]] = {

    val updatedEntry: ApplicationStateMapEntry = {
      val utr: UntypedRef =
        UntypedRef.makeFromRefToEntity[V]( entityToUpdate.refToEntity )

      val entityAsString: EntityAsString = entityToUpdate.entityAsString()

      val applicationStateEntry =
        ApplicationStateMapEntry( utr, entityAsString )
      applicationStateEntry
    }

    val res: Future[UpdateEntity_Command_Response] = {

      val commandToExecute = UpdateEntity_Command( updatedEntry )

      println( s"""
          |
          |vvvvvvvvvvvvvvvvvvvvvvv-------------------------------
          |
          |We are now in updateEntity, inside PlainFunctionInterfaceToPersistentActor
          |
          |we will "ask" the persistent actor to execute the following
          |UpdateEntityCommand :
          |
          |$commandToExecute
          |
          |
          |or pretty printed:
          |
          |${PrettyPrint.prettyPrint( commandToExecute )}
          |
          |
          |^^^^^^^^^^^^^^^^^^^^^^^-------------------------------
          |
        """.stripMargin )

      ask( actor, commandToExecute )(
        Timeout.durationToTimeout( 1 seconds )
      ).mapTo[UpdateEntity_Command_Response]

    }

    res.map( (x: UpdateEntity_Command_Response) => x.result )

  }

}
