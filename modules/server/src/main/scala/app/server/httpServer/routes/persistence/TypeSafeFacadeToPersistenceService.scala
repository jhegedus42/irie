package app.server.httpServer.routes.persistence

import app.server.httpServer.routes.persistence.notTypeSafeWorld.{
  ApplicationStateMapEntry,
  NotTypeSafeInterfaceToPersistentActor,
  StateChange
}
import app.server.httpServer.routes.persistence.notTypeSafeWorld.state.UntypedRef
import app.server.httpServer.routes.persistence.notTypeSafeWorld.persistentActor.{
  Errors,
  Responses
}
import app.shared.entity.Entity
import app.shared.entity.asString.EntityAsJSON
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.{
  RefToEntityWithVersion,
  RefToEntityWithoutVersion
}
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

private[routes] case class TypeSafeFacadeToPersistenceService(
    context: ExecutionContextExecutor
) {

  implicit val context_as_implicit = context

  private val simpleFunctionInterfaceToPersistentActor
    : NotTypeSafeInterfaceToPersistentActor = {
    NotTypeSafeInterfaceToPersistentActor()
  }

  def getEntityWithLatestVersion[V <: EntityValue[V]](
      ref:       RefToEntityWithoutVersion[V]
  )( implicit d: Decoder[Entity[V]] ): Future[Option[Entity[V]]] = {
    ??? //todo-now
  }

  def createAndStoreNewEntity[V <: EntityValue[V]: ClassTag]( value: V )(
      implicit encoder: Encoder[Entity[V]]
  ): Future[( StateChange, Entity[V] )] = {

    val entity: Entity[V] = Entity.makeFromValue( value )

    val res: Future[StateChange] =
      simpleFunctionInterfaceToPersistentActor.insertEntity[V]( entity )

    res.map( x => ( x, entity ) )
  }

  def updateEntity[V <: EntityValue[V]: ClassTag]( entity: Entity[V] )(
      implicit encoder: Encoder[Entity[V]]
  ): Future[( StateChange, Entity[V] )] = {

    val res: Future[Either[Errors.EntityUpdateVersionError, StateChange]] =
      simpleFunctionInterfaceToPersistentActor.updateEntity( entity )

    val res2: Future[( StateChange, Entity[V] )] = res.flatMap(
      (x: Either[Errors.EntityUpdateVersionError, StateChange]) => {
        x match {
          case Left( value ) =>
            Future.failed(
              new Exception(
                "There was some problem with the versions or something when" +
                  s"trying to update the entity : \n$entity "
              )
            )
          case Right( value ) => Future.successful( ( value, entity ) )
        }

      }
    )
    res2
  }

  private[this] def getEntityWithVersion[V <: EntityValue[V]](
      ref: RefToEntityWithVersion[V]
  )(
      implicit d: Decoder[Entity[V]]
  ): Future[Option[Entity[V]]] = {

    val eventualGetStateResult
      : Future[Responses.GetFullApplicationState_Command_Response] =
      simpleFunctionInterfaceToPersistentActor.getState

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

}
