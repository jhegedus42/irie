package app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps

import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.ElementaryPersistenceOperation
import app.shared.entity.entityValue.EntityValue

// todo-now-2 implement this
/**
  * To be implemented, get inspiration from
  * [[app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.InsertPO]]
  *
  */

trait UpdatePO[V<:EntityValue[V]] extends ElementaryPersistenceOperation[V]

object UpdatePO {

}

