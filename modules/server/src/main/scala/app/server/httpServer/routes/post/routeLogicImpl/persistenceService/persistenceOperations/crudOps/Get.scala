package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.PersistenceOperation.{OperationError, OperationResult, OperatonParameter}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.{PersOpExecutor, PersistenceOperation}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithoutVersion
import io.circe.Decoder

import scala.concurrent.Future

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

  case class GetPersOpExecutorImpl[V <: EntityValue[V]]()
      extends PersOpExecutor[GetOp[V]] {

    def getEntityWithLatestVersion[EV <: EntityValue[EV]](
        ref: RefToEntityWithoutVersion[EV]
    )(
        implicit
        d: Decoder[Entity[EV]]
    ): Future[Option[Entity[EV]]] = {
      ??? // todo-right-now-0
    }

    override def execute(
        par: Get.GetOpPar[V]
    ): Future[Get.GetOpRes[V]] = ??? // todo-right-now
  }
}
