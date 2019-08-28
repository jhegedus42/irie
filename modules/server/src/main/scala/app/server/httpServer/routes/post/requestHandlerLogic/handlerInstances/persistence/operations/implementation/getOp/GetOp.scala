package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.implementation.getOp

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.Operation
import app.shared.entity.entityValue.EntityValue

trait GetOp[V <: EntityValue[V]] extends Operation

