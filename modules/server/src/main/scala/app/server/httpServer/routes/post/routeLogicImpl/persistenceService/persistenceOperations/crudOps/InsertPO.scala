package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.ElementaryPersistenceOperation
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.ElementaryPersistenceOperation.{OperationError, OperationResult, OperatonParameter}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.InsertPO.{InsertPOPar, InsertPORes}
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ.OCCVersion
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.{POExecutor, ElementaryPersistenceOperation}
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


trait InsertPO[V <: EntityValue[V]] extends ElementaryPersistenceOperation[V] {
  override type Res = InsertPORes[V]
  override type Par = InsertPOPar[V]
}

object InsertPO {

  case class InsertPOPar[V <: EntityValue[V]](
      val value: V
  ) extends OperatonParameter

  case class InsertPORes[V <: EntityValue[V]](
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
  ) extends POExecutor[V,InsertPO[V]] {

    override def execute(
        par: InsertPO.InsertPOPar[V]
    )(
        implicit pa: PersistentActorWhisperer
    ): Future[InsertPO.InsertPORes[V]] = {

      val in: InsertPOPar[V] = par

      implicit val d:   Decoder[Entity[V]] = decoder
      implicit val e:   Encoder[Entity[V]] = encoder
      implicit val cti: ClassTag[V]        = ct
      implicit val ec: ExecutionContextExecutor = pa.executionContext

      val res0: Future[Option[Entity[V]]] =
        pa.insertNewEntity(par.value)

      val res1:        Future[Entity[V]]        = res0.map(_.get)

      def f(in: Entity[V]): Either[OperationError, Entity[V]] =
        Right(in)

      val res2: Future[Either[OperationError, Entity[V]]] =
        res1.map(f(_))

      val res: Future[InsertPO.InsertPORes[V]] = res2.map(InsertPORes[V](_))
      res
    }

    // call getEntityWithLatestVersion and massage result
    override def getOCCVersion: OCCVersion = ???
  }
}
