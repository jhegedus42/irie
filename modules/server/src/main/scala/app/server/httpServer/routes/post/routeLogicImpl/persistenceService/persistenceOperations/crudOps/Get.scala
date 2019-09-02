package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.PersistenceOperation.{
  OperationError,
  OperationResult,
  OperatonParameter
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ.OCCVersion
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.{
  PersistenceOperation,
  PersistenceOperationExecutorTypeClass
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.PersistentActorWhisperer
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithoutVersion
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}

object Get {

  trait GetOp[V <: EntityValue[V]] extends PersistenceOperation {
    override type Res = GetOpRes[V]
    override type Par = GetOpPar[V]
  }

  case class GetOpPar[V <: EntityValue[V]](
      val ref: RefToEntityWithoutVersion[V]
  ) extends OperatonParameter

  case class GetOpRes[V <: EntityValue[V]](
      res: Either[OperationError[GetOp[V]], Entity[V]]
  ) extends OperationResult

  case class GetPersistenceOperationExecutorTypeClassImpl[V <: EntityValue[V]](
      decoder: Decoder[Entity[V]]
  ) extends PersistenceOperationExecutorTypeClass[GetOp[V]] {

    override def execute(
        par: Get.GetOpPar[V]
    )(
        implicit pa: PersistentActorWhisperer
    ): Future[Get.GetOpRes[V]] = {
      val in: GetOpPar[V] = par

      implicit val d: Decoder[Entity[V]] =decoder

      val res0: Future[Option[Entity[V]]] =
        pa.getEntityWithLatestVersion(par.ref)
      // todo later fix this :

      implicit val ec: ExecutionContextExecutor = pa.executionContext
      val res1:        Future[Entity[V]]        = res0.map(_.get)

      def f(in: Entity[V]): Either[OperationError[GetOp[V]], Entity[V]] =
        Right(in)

      val res2: Future[Either[OperationError[GetOp[V]], Entity[V]]] =
        res1.map(f(_))

      val res: Future[Get.GetOpRes[V]] = res2.map(GetOpRes[V](_))
      res
    }

    // call getEntityWithLatestVersion and massage result
    override def getOCCVersion: OCCVersion = ???
  }
}
