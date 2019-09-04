package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.PersistenceOperation
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.PersistenceOperation.{OperationError, OperationResult, OperatonParameter}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ.OCCVersion
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.{PersistenceOperation, PersistenceOperationExecutorTypeClass}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.PersistentActorWhisperer
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithoutVersion
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag
trait Insert extends PersistenceOperation

object Insert {

  trait InsertOp[V <: EntityValue[V]] extends PersistenceOperation {
    override type Res = InsertOpRes[V]
    override type Par = InsertOpPar[V]
  }

  case class InsertOpPar[V <: EntityValue[V]](
      val value: V
  ) extends OperatonParameter

  case class InsertOpRes[V <: EntityValue[V]](
      res: Either[OperationError[InsertOp[V]], Entity[V]]
  ) extends OperationResult

  case class InsertPersistenceOperationExecutorTypeClassImpl[V <: EntityValue[V]](
      decoder: Decoder[Entity[V]],ct:ClassTag[V]
  ) extends PersistenceOperationExecutorTypeClass[InsertOp[V]] {

    override def execute(
        par: Insert.InsertOpPar[V]
    )(
        implicit pa: PersistentActorWhisperer
    ): Future[Insert.InsertOpRes[V]] = {
      val in: InsertOpPar[V] = par

      implicit val d: Decoder[Entity[V]] =decoder
      implicit val cti: ClassTag[V] =ct

            val res0: Future[Option[Entity[V]]] =
              pa.insertNewEntity(par.value)

      implicit val ec: ExecutionContextExecutor = pa.executionContext
      val res1:        Future[Entity[V]]        = res0.map(_.get)

      def f(in: Entity[V]): Either[OperationError[InsertOp[V]], Entity[V]] =
        Right(in)

      val res2: Future[Either[OperationError[InsertOp[V]], Entity[V]]] =
        res1.map(f(_))

      val res: Future[Insert.InsertOpRes[V]] = res2.map(InsertOpRes[V](_))
      res
    }

    // call getEntityWithLatestVersion and massage result
    override def getOCCVersion: OCCVersion = ???
  }
}