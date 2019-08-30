package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances

import app.server.httpServer.routes.post.requestHandlerLogic.Logic
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.PersistenceService
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.Get
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.Get.GetOp
import app.shared.comm.postRequests.GetEntityReq
import app.shared.comm.postRequests.GetEntityReq.{
  GetEntityReqPar,
  GetEntityReqRes
}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.Entity
import app.shared.entity.refs.{
  RefToEntityWithVersion,
  RefToEntityWithoutVersion
}
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.Try

case class GetEntityLogic[V <: EntityValue[V]](
    persistenceModule: PersistenceService,
    d:                 Decoder[Entity[V]],
    contextExecutor:   ExecutionContextExecutor
) extends Logic[GetEntityReq[V]] {

  implicit val ce: ExecutionContextExecutor = contextExecutor

  object Helpers {

    def getResultFromPersistenceModule(
        param: GetEntityReqPar[V]
    ): Future[Get.GetOpRes[V]] =
      persistenceModule.operationExecutor[GetOp[V]](
        Get.GetOpPar(param.refToEntityWithoutVersion)
      )

    def convert(x: Get.GetOpRes[V]): Option[GetEntityReqRes[V]] = {
      ??? //todo-right-now-1
    }

    def log(
        param: GetEntityReqPar[V],
        x:     Future[Some[GetEntityReqRes[V]]]
    ): Unit = {
      x.onComplete((x: Try[Some[GetEntityReqRes[V]]]) => {
        val s2 =
          s"""
             |
             |
             |vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
             |
             |
             |r3 has completed in `GetEntityLogic`
             |it was called with param:
             |$param
             |
             |it resulted in a :
             |$x
             |
             |
             |^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
             |
             |
           """.stripMargin
        println(s2)
      })
    }
  }

  override def getResult(
      param: GetEntityReqPar[V]
  ): Future[Option[GetEntityReqRes[V]]] = {
    Helpers
      .getResultFromPersistenceModule(param)
      .map(Helpers.convert(_))
  }

}