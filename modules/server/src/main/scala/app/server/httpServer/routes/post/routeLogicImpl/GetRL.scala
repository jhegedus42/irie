package app.server.httpServer.routes.post.routeLogicImpl

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.PersistentServiceProvider
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.EPOPExecutor
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.GetEPOP
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.GetEPOP.GetOp
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

case class GetRL[V <: EntityValue[V]](
    persistenceModule: PersistentServiceProvider,
//    d:                 Decoder[Entity[V]],
    dv:                 Decoder[V],
    contextExecutor:   ExecutionContextExecutor,
) extends RouteLogic[GetEntityReq[V]] {

  implicit val ce: ExecutionContextExecutor = contextExecutor

  object Helpers {

    def getResultFromPersistenceModule(
        param: GetEntityReqPar[V]
    ): Future[GetEPOP.GetOpRes[V]] = {
      implicit val di: Decoder[V] =dv
      implicit val i: GetEPOP.GetEPOPExecutorImpl[V] =
        EPOPExecutor.getOperationInstance[V]

      persistenceModule.executePO[V,GetOp[V]](
        GetEPOP.GetOpPar(param.refToEntityWithoutVersion)
      )
    }

    def convert(x: GetEPOP.GetOpRes[V]): Option[GetEntityReqRes[V]] = {

      val opEnt: Option[Entity[V]] = x.res.toOption
      val res:   Option[Entity[V]] = opEnt
      Some(GetEntityReqRes(res))
      // todo-later - fix all the unsafeness in this method
      //  lot of things can throw, we should use proper
      //  map / flatMap etc... to handle/flatten the Options / Eithers
      //  in a more meaningful way
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

  override def getHttpReqResult(
      param: GetEntityReqPar[V]
  ): Future[Option[GetEntityReqRes[V]]] = {
    Helpers
      .getResultFromPersistenceModule(param)
      .map(Helpers.convert(_))
  }

}
