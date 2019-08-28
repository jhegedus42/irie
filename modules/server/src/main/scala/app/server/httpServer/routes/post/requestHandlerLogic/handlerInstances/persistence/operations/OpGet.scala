package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations

import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.refs.RefToEntityWithoutVersion

object OpGet {

  trait GetOp[V <: EntityValue[V]] extends Operation {
    override type OP  = this.type
    override type Res = GetOpRes[V]
    override type Par = GetOpPar[V]
  }

  case class GetOpPar[V <: EntityValue[V]](
    val ref: RefToEntityWithoutVersion[V])
      extends OperatonParameter

  case class GetOpRes[V <: EntityValue[V]](
    res: Either[OperationError[GetOp[V]], Entity[V]])
      extends OperationResult

}
