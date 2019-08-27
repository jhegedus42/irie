package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.actor.FacadeToStupidPersistentActor
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithoutVersion
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

private[routes] case class PersistenceService(
    context: ExecutionContextExecutor
) {

  implicit val context_as_implicit = context

  private val simpleFunctionInterfaceToPersistentActor: FacadeToStupidPersistentActor = {
    FacadeToStupidPersistentActor( context )
  }

  def getEntityWithLatestVersion[V <: EntityValue[V]](
      ref:       RefToEntityWithoutVersion[V]
  )( implicit d: Decoder[Entity[V]] ): Future[Option[Entity[V]]] = {
    ??? //todo-now
  }

  def createAndStoreNewEntity[V <: EntityValue[V]: ClassTag]( value: V )(
      implicit encoder: Encoder[Entity[V]]
  ): Future[(Entity[V])] = {
//
//    val entity: Entity[V] = Entity.makeFromValue( value )
//
//    val res: Future[AreWeHappyInTheUnderWorld] =
//      simpleFunctionInterfaceToPersistentActor.insertEntity[V]( entity )
//
//    res.map( x => (x,entity) )
    ???
  }

  def updateEntity[V <: EntityValue[V]: ClassTag]( entity: Entity[V] )(
      implicit encoder: Encoder[Entity[V]]
  ): Future[Entity[V]] = {
//
//    val res: Future[Right[PersistenceError,]] =
//      simpleFunctionInterfaceToPersistentActor.updateEntity( entity )
//
//    val res2: Future[Entity[V]] = res.flatMap(
//      (x: (AreWeHappyInTheUnderWorld)) => {
//        x.why match {
//          case Left( value ) =>
//            Future.failed(
//              new Exception(
//                s"""
//      |
//      |
//      |  We are not very happy. Something went wrong.
//      |
//      |  "There was some problem with the versions or something when" +
//      |    s"trying to update the entity : \n$entity "
//      |
//      |  Also, there is the following reason too, why we are
//      |  not so happy: $value
//      |
//      |
//    """.stripMargin
//              )
//            )
//          case Right( value ) => Future.successful( entity )
//        }
//
//      }
//    )
//
//    res2
    ???
  }

}
