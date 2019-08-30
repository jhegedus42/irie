package app.server.httpServer.routes.post.logic.persistence

import akka.actor.ActorRef
import app.server.httpServer.routes.post.logic.persistence.operations.crudOps.Get
import app.server.httpServer.routes.post.logic.persistence.operations.crudOps.Get.GetOp
import app.server.httpServer.routes.post.logic.persistence.operations.crudOps.persistentActor.logic.PersistentActorImp
import app.server.httpServer.routes.post.logic.persistence.operations.{
  OpExecutor,
  Operation
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.{
  RefToEntityWithVersion,
  RefToEntityWithoutVersion
}
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.postfixOps
import scala.reflect.ClassTag

private[routes] case class PersistenceService(
    context: ExecutionContextExecutor
) {

  val actor: ActorRef = PersistentActorImp.getActor(
    "the_one_and_only_parsistent_actor"
  )

  implicit val context_as_implicit = context

  def operationExecutor[OP <: Operation](
      par:       OP#Par
  )(implicit ex: OpExecutor[OP]): Future[OP#Res] = { ex.execute(par) }

}

//  import akka.pattern.ask
//  import akka.util.Timeout
//
//  import scala.concurrent.duration._
//
//  object SendAskToActor {
//
//    private[persistence] case class InterfaceToActor(
//        context: ExecutionContextExecutor
//    ) {
//
//      /**
//        * Inserts an entity into the Map containeing the Appliacation State.
//        *
//        * @param entityToInsert Entity to insert. (This should not exist yet in the persistent actor).
//        *                       It has to have version number 0.
//        * @tparam V value contained in the entity.
//        * @return the application state before and after the insertion :
//        *         (in a StateChange object, first element is the state before,
//        *         second one is the state after the insertion).
//        */
//      def insertEntity[V <: EntityValue[V]](
//          entityToInsert: Entity[V]
//      )(
//          implicit
//          encoder: Encoder[Entity[V]]
//      ): Future[Unit] = {
//
//        //    assert( entityToInsert.refToEntity.entityVersion.versionNumberLong == 0 )
//        //
//        //    val newEntry: ApplicationStateMapEntry = {
//        //      val utr: UntypedRef =
//        //        UntypedRef.makeFromRefToEntity[V]( entityToInsert.refToEntity )
//        //
//        //      val entityAsString: EntityAsString = entityToInsert.entityAsString()
//        //
//        //      val applicationStateEntry =
//        //        ApplicationStateMapEntry( utr, entityAsString )
//        //      applicationStateEntry
//        //    }
//        //
//        //    val res: Future[InsertNewEntity_Command_Response] =
//        //      ask( actor, InsertNewEntity_Command( newEntry ) )(
//        //        Timeout.durationToTimeout( 1 seconds )
//        //      ).mapTo[InsertNewEntity_Command_Response]
//        //
//        //    res.map( x => x.stateChange )
//
//        ???
//      }
//
//      //  def updateEntity(rfvd: RefValDyn): Future[UpdateEntityPAResponse] =
//      //    ask(actor, UpdateEntityPACommand(rfvd))(Timeout.durationToTimeout(1 seconds))
//      //      .mapTo[UpdateEntityPAResponse]
//
//      def updateEntity[V <: EntityValue[V]](
//          entityToUpdate: Entity[V]
//      )( //      implicit executionContextExecutor:                 ExecutionContextExecutor,
//          implicit
//          encoder: Encoder[Entity[V]]
//      ): Future[Entity[V]] = {
//
//        //    val updatedEntry: ApplicationStateMapEntry = {
//        //      val utr: UntypedRef =
//        //        UntypedRef.makeFromRefToEntity[V]( entityToUpdate.refToEntity )
//        //
//        //      val entityAsString: EntityAsString = entityToUpdate.entityAsString()
//        //
//        //      val applicationStateEntry =
//        //        ApplicationStateMapEntry( utr, entityAsString )
//        //      applicationStateEntry
//        //    }
//        //
//        //    val res: Future[UpdateEntity_Command_Response] = {
//        //
//        //      val commandToExecute = UpdateEntity_Command( updatedEntry )
//        //
//        //      println( s"""
//        //          |
//        //          |vvvvvvvvvvvvvvvvvvvvvvv-------------------------------
//        //          |
//        //          |We are now in updateEntity, inside PlainFunctionInterfaceToPersistentActor
//        //          |
//        //          |we will "ask" the persistent actor to execute the following
//        //          |UpdateEntityCommand :
//        //          |
//        //          |$commandToExecute
//        //          |
//        //          |
//        //          |or pretty printed:
//        //          |
//        //          |${PrettyPrint.prettyPrint( commandToExecute )}
//        //          |
//        //          |
//        //          |^^^^^^^^^^^^^^^^^^^^^^^-------------------------------
//        //          |
//        //        """.stripMargin )
//        //
//        //      ask( actor, commandToExecute )(
//        //        Timeout.durationToTimeout( 1 seconds )
//        //      ).mapTo[UpdateEntity_Command_Response]
//        //
//        //    }
//        //
//        //    res.map( (x: UpdateEntity_Command_Response) => x.result )
//
//        ???
//      }
//
//      def getEntityWithVersion[V <: EntityValue[V]](
//          ref: RefToEntityWithVersion[V]
//      )(
//          implicit
//          d: Decoder[Entity[V]]
//      ): Future[Option[Entity[V]]] = {
//
//        //    val eventualGetStateResult
//        //      : Future[Responses.GetFullApplicationState_Command_Response] =
//        //      getState
//        //
//        //    val untypedRef: UntypedRef = UntypedRef.makeFromRefToEntity( ref )
//        //
//        //    val r1: Future[Option[ApplicationStateMapEntry]] =
//        //      eventualGetStateResult.map( r => r.state.map.get( untypedRef ) )
//        //
//        //    val r2: Future[ApplicationStateMapEntry] =
//        //      r1.flatMap( (x: Option[ApplicationStateMapEntry]) => {
//        //        x match {
//        //          case Some( value ) => Future.successful( value )
//        //          case None =>
//        //            Future.failed(
//        //              new Exception(
//        //                "val r2 in PersistenceModule.getEntity has failed. " +
//        //                  "Please see the source code for details."
//        //              )
//        //            )
//        //        }
//        //      } )
//        //
//        //    val r3: Future[EntityAsJSON] = r2.map( (x: ApplicationStateMapEntry) => {
//        //      val json: EntityAsJSON = x.entityAsString.entityAsJSON
//        //      json
//        //    } )
//        //
//        //    val r4: Future[Option[Entity[V]]] = r3.map( EntityAsJSON.getEntity[V]( _ ) )
//        //
//        //    val r5: Future[Entity[V]] = r4.flatMap( (x: Option[Entity[V]]) => {
//        //      x match {
//        //        case Some( value ) => Future.successful( value )
//        //        case None =>
//        //          Future.failed(
//        //            new Exception(
//        //              "val r5 in PersistenceModule.getEntity has failed. " +
//        //                "Please see the source code for details."
//        //            )
//        //          )
//        //      }
//        //
//        //    } )
//        //    r4
//
//        ??? // todo-right-now
//      }
//
//    }
//  }
//def createAndStoreNewEntity[
//V <: EntityValue[V]: ClassTag
//](value: V)(
//implicit
//encoder: Encoder[Entity[V]]
//): Future[(Entity[V])] = {
//  //
//  //    val entity: Entity[V] = Entity.makeFromValue( value )
//  //
//  //    val res: Future[AreWeHappyInTheUnderWorld] =
//  //      persActorWrapped.insertEntity[V]( entity )
//  //
//  //    res.map( x => (x,entity) )
//  ???
//}
//
//  def updateEntity[V <: EntityValue[V]: ClassTag](
//  entity: Entity[V]
//  )(
//  implicit
//  encoder: Encoder[Entity[V]]
//  ): Future[Entity[V]] = {
//  //
//  //    val res: Future[Right[PersistenceError,]] =
//  //      persActorWrapped.updateEntity( entity )
//  //
//  //    val res2: Future[Entity[V]] = res.flatMap(
//  //      (x: (AreWeHappyInTheUnderWorld)) => {
//  //        x.why match {
//  //          case Left( value ) =>
//  //            Future.failed(
//  //              new Exception(
//  //                s"""
//  //      |
//  //      |
//  //      |  We are not very happy. Something went wrong.
//  //      |
//  //      |  "There was some problem with the versions or something when" +
//  //      |    s"trying to update the entity : \n$entity "
//  //      |
//  //      |  Also, there is the following reason too, why we are
//  //      |  not so happy: $value
//  //      |
//  //      |
//  //    """.stripMargin
//  //              )
//  //            )
//  //          case Right( value ) => Future.successful( entity )
//  //        }
//  //
//  //      }
//  //    )
//  //
//  //    res2
//  ???
//}
