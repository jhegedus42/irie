package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.GetPO
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.occ.OCCVersion
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

trait POExecutor[V<:EntityValue[V],OP <: ElementaryPersistenceOperation[V]] {
  def execute(par: OP#Par)(
      implicit
      typeSafeAccessToPersistentActorProvider: PersistentActorWhisperer
      // ElKelKaposzTasTalanItHatatLanSagosKodAsAitokErt
  ):                 Future[OP#Res]
  def getOCCVersion: OCCVersion
}

object POExecutor {

  def getOperationInstance[V <: EntityValue[V]](
      implicit d: Decoder[Entity[V]]
  ) =
    GetPO.GetPOExecutorImpl[V](d)

}
