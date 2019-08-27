package app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor

import akka.actor.ActorRef
import app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor.state.ApplicationStateMapEntry
import app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor.state.refs.UntypedRef
import app.server.utils.PrettyPrint
import app.shared.entity.Entity
import app.shared.entity.asString.{EntityAsJSON, EntityAsString}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithVersion
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.postfixOps


private[typesafePersistence] case class FacadeToStupidPersistentActor(
        context: ExecutionContextExecutor
) {

  import akka.pattern.ask
  import akka.util.Timeout

  import scala.concurrent.duration._
    implicit val context_as_implicit = context

  val actor: ActorRef =
    PersistentActorForOurApp.getActor( "the_one_and_only_parsistent_actor" )

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
      implicit encoder:                                           Encoder[Entity[V]]
  ): Future[AreWeHappyInTheUnderWorld] = {

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

  //  def updateEntity(rfvd: RefValDyn): Future[UpdateEntityPAResponse] =
  //    ask(actor, UpdateEntityPACommand(rfvd))(Timeout.durationToTimeout(1 seconds))
  //      .mapTo[UpdateEntityPAResponse]

  def updateEntity[V <: EntityValue[V]]( entityToUpdate: Entity[V] )(
//      implicit executionContextExecutor:                 ExecutionContextExecutor,
      implicit encoder:                                           Encoder[Entity[V]]
  ): Future[Entity[V]] = {

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

  def getEntityWithVersion[V <: EntityValue[V]](
      ref: RefToEntityWithVersion[V]
  )(
      implicit d: Decoder[Entity[V]]
  ): Future[Option[Entity[V]]] = {

    val eventualGetStateResult
      : Future[Responses.GetFullApplicationState_Command_Response] =
      getState

    val untypedRef: UntypedRef = UntypedRef.makeFromRefToEntity( ref )

    val r1: Future[Option[ApplicationStateMapEntry]] =
      eventualGetStateResult.map( r => r.state.map.get( untypedRef ) )

    val r2: Future[ApplicationStateMapEntry] =
      r1.flatMap( (x: Option[ApplicationStateMapEntry]) => {
        x match {
          case Some( value ) => Future.successful( value )
          case None =>
            Future.failed(
              new Exception(
                "val r2 in PersistenceModule.getEntity has failed. " +
                  "Please see the source code for details."
              )
            )
        }
      } )

    val r3: Future[EntityAsJSON] = r2.map( (x: ApplicationStateMapEntry) => {
      val json: EntityAsJSON = x.entityAsString.entityAsJSON
      json
    } )

    val r4: Future[Option[Entity[V]]] = r3.map( EntityAsJSON.getEntity[V]( _ ) )

    val r5: Future[Entity[V]] = r4.flatMap( (x: Option[Entity[V]]) => {
      x match {
        case Some( value ) => Future.successful( value )
        case None =>
          Future.failed(
            new Exception(
              "val r5 in PersistenceModule.getEntity has failed. " +
                "Please see the source code for details."
            )
          )
      }

    } )

    r4
  }

  private def getState
    : Future[Responses.GetFullApplicationState_Command_Response] =
    ask( actor, GetFullApplicationState_Command )(
      Timeout.durationToTimeout( 1 seconds )
    ).mapTo[Responses.GetFullApplicationState_Command_Response]
    // todo-next - put this into some simple Pers Actor Wrapper
    //  this level should not contain ask-s
}
