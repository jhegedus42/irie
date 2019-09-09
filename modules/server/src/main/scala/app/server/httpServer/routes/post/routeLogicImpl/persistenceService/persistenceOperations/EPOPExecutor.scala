package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.GetEPOP
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ.OCCVersion
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.testRelatedOperations.Reset.ResetStateEPOP
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistentActor.PersistentActorWhisperer
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import io.circe.Decoder

import scala.concurrent.Future
import scala.reflect.ClassTag

// todo-later, please see the doc below:

/**
  *
  * Type class that contains the logic on how
  * to execute a persistence operation.
  *
  *
  * todo-later :
  * Perhaps this should be merged with
  * [[app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.ElementaryPersistenceOperation]]
  *
  * @tparam OP
  */
trait EPOPExecutor[V <: EntityValue[V], OP <: ElementaryPersistenceOperation[
  V
]] {
  def execute(par: OP#Par)(
      implicit
      typeSafeAccessToPersistentActorProvider: PersistentActorWhisperer
      // ElKelKaposzTasTalanItHatatLanSagosKodAsAitokErt
  ): Future[OP#Res]
  //todo-later - move this method into
  //  app.server.httpServer.routes.post.
  //  routeLogicImpl.persistenceService.
  //  persistenceOperations.ElementaryPersistenceOperation
  def getOCCVersion: OCCVersion
}

object EPOPExecutor {

  def getOperationInstance[V <: EntityValue[V]](
      implicit d: Decoder[V]
  ): GetEPOP.GetEPOPExecutorImpl[V] =
    GetEPOP.GetEPOPExecutorImpl[V](d)

//  implicit val instResetState: EPOPExecutor[Nothing, ResetStateEPOP] =
//    ResetStateEPOP.ResetEPOPSt

}
