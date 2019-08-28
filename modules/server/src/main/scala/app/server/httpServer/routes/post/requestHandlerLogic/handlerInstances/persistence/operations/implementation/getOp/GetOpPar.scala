package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.implementation.getOp

import app.shared.comm.PostRequest.Parameter
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithoutVersion

case class GetOpPar[V <: EntityValue[V]](
  val ref: RefToEntityWithoutVersion[V])
