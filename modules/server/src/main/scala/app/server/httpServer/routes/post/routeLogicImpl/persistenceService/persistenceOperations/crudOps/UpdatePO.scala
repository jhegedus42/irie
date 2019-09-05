package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.ElementaryPersistenceOperation
import app.shared.entity.entityValue.EntityValue
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.ElementaryPersistenceOperation
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.ElementaryPersistenceOperation.{
  OperationError,
  OperationResult,
  OperatonParameter
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.UpdatePO.{
  UpdatePOPar,
  UpdatePORes
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ.OCCVersion
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.{
  ElementaryPersistenceOperation,
  POExecutor
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.PersistentActorWhisperer
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithoutVersion
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

// todo-now-2 implement this
/**
  * To be implemented, get inspiration from
  * [[app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.UpdatePO]]
  *
  */
trait UpdatePO[V <: EntityValue[V]] extends ElementaryPersistenceOperation[V] {
  override type Res = UpdatePORes[V]
  override type Par = UpdatePOPar[V]

}

object UpdatePO {

  case class UpdatePOPar[V <: EntityValue[V]](
      val
      currentEntity: Entity[V],
      val newValue:  V
  ) extends OperatonParameter

  case class UpdatePORes[V <: EntityValue[V]](
      res: Either[OperationError, Entity[V]]
  ) extends OperationResult

  /**
    *
    * Typeclass Instance.
    *
    * @param decoder
    * @param encoder
    * @param ct
    * @tparam V
    */
  case class Executor[V <: EntityValue[
    V
  ]](
      decoder: Decoder[Entity[V]],
      encoder: Encoder[Entity[V]],
      ct:      ClassTag[V]
  ) extends POExecutor[V, UpdatePO[V]] {

//    override def execute(par: UpdatePO[V]#Par)(
//        implicit typeSafeAccessToPersistentActorProvider: PersistentActorWhisperer
//        implicit pa: PersistentActorWhisperer
//    ): Future[UpdatePO[V]#Res] = {

    override def execute(
        par: UpdatePO.UpdatePOPar[V]
    )(
        implicit pa: PersistentActorWhisperer
    ): Future[UpdatePO.UpdatePORes[V]] = {

      val in: UpdatePOPar[V] = par

      implicit val d:   Decoder[Entity[V]]       = decoder
      implicit val e:   Encoder[Entity[V]]       = encoder
      implicit val cti: ClassTag[V]              = ct
      implicit val ec:  ExecutionContextExecutor = pa.executionContext

      val res0: Future[Option[Entity[V]]] =
//        pa.insertNewEntity(par.value)
        pa.updateEntity[V](par.currentEntity, par.newValue)

      val res1: Future[Entity[V]] = res0.map(_.get)

      def f(in: Entity[V]): Either[OperationError, Entity[V]] =
        Right(in)

      val res2: Future[Either[OperationError, Entity[V]]] =
        res1.map(f(_))

      val res: Future[UpdatePO.UpdatePORes[V]] = res2.map(UpdatePORes[V](_))
      res
    }

    // call getEntityWithLatestVersion and massage result
    override def getOCCVersion: OCCVersion = ???
  }
}
