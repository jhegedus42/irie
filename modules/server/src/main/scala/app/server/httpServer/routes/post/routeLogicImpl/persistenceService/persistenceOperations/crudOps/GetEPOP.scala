package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.ElementaryPersistenceOperation.{
  OperationError,
  OperationResult,
  OperatonParameter
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ.OCCVersion
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.{
  ElementaryPersistenceOperation,
  EPOPExecutor
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.PersistentActorWhisperer
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithoutVersion
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}

object GetEPOP {

  trait GetOp[V <: EntityValue[V]] extends ElementaryPersistenceOperation[V] {
    override type Res = GetOpRes[V]
    override type Par = GetOpPar[V]
  }

  case class GetOpPar[V <: EntityValue[V]](
      val ref: RefToEntityWithoutVersion[V]
  ) extends OperatonParameter

  case class GetOpRes[V <: EntityValue[V]](
      res: Either[OperationError, Entity[V]]
  ) extends OperationResult

  case class GetEPOPExecutorImpl[V <: EntityValue[V]](
      decoder: Decoder[V]
  ) extends EPOPExecutor[V, GetOp[V]] {

    override def execute(
        par: GetEPOP.GetOpPar[V]
    )(
        implicit pa: PersistentActorWhisperer
    ): Future[GetEPOP.GetOpRes[V]] = {
      val in: GetOpPar[V] = par

      implicit val d: Decoder[V] = decoder

      val res0: Future[Option[Entity[V]]] =
        pa.getEntityWithLatestVersion(par.ref)


      // todo later fix this :

      implicit val ec: ExecutionContextExecutor = pa.executionContext

      val res1:        Future[Entity[V]]        = res0.map(_.get)

      def f(in: Entity[V]): Either[OperationError, Entity[V]] =
        Right(in)

      val res2: Future[Either[OperationError, Entity[V]]] =
        res1.map(f(_))

      val res: Future[GetEPOP.GetOpRes[V]] = res2.map(GetOpRes[V](_))
      res
    }

    // call getEntityWithLatestVersion and massage result
    override def getOCCVersion: OCCVersion = ???
  }

}
