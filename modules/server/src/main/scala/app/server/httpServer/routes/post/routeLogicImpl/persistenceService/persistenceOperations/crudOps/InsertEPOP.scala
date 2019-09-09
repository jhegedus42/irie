package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.ElementaryPersistenceOperation
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.ElementaryPersistenceOperation.{
  OperationError,
  OperationResult,
  OperatonParameter
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.InsertEPOP.{
  InsertPOPar,
  InsertPORes
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ.OCCVersion
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.{
  EPOPExecutor,
  ElementaryPersistenceOperation
}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.PersistentActorWhisperer
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithoutVersion
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag

/**
  *
  * Persistence Operation which creates a new Entity.
  *
  */
trait InsertEPOP[V <: EntityValue[V]]
    extends ElementaryPersistenceOperation[V] {
  override type Res = InsertPORes[V]
  override type Par = InsertPOPar[V]
}

object InsertEPOP {

  case class InsertPOPar[V <: EntityValue[V]](val value: V)
      extends OperatonParameter

  case class InsertPORes[V <: EntityValue[V]](
    res: Either[OperationError, Entity[V]])
      extends OperationResult

  /**
    * Typeclass Instance.
    */
  case class Executor[V <: EntityValue[V]](
  )(
    implicit
    decoder:  Decoder[Entity[V]],
    encoder:  Encoder[Entity[V]],
    encoderV: Encoder[V],
    classTag: ClassTag[V],
    ec:       ExecutionContextExecutor)
      extends EPOPExecutor[V, InsertEPOP[V]] {

    override def execute(
      par: InsertEPOP.InsertPOPar[V]
    )(
      implicit pa: PersistentActorWhisperer
    ): Future[InsertEPOP.InsertPORes[V]] = {

      pa.insertNewEntity(par.value)
        .map(_.get)
        .map(Right(_))
        .map(InsertPORes[V](_))

    }

    // call getEntityWithLatestVersion and massage result
    override def getOCCVersion: OCCVersion = ???
  }
}
