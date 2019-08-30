package app.server.httpServer.routes.post.logic

import app.server.httpServer.routes.post.Logic
import app.server.httpServer.routes.post.logic.persistence.PersistenceService
import app.server.httpServer.routes.post.logic.persistence.operations.crudOps.Get
import app.server.httpServer.routes.post.logic.persistence.operations.crudOps.Get.GetOp
import app.shared.comm.postRequests.GetEntityReq
import app.shared.comm.postRequests.GetEntityReq.{GetEntityReqPar, GetEntityReqRes}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.Entity
import app.shared.entity.refs.{RefToEntityWithVersion, RefToEntityWithoutVersion}
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.Try

case class GetLogic[V <: EntityValue[V]](
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

      val opEnt=x.res.toOption
      val res: Option[Entity[V]] = opEnt
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

  override def getResult(
      param: GetEntityReqPar[V]
  ): Future[Option[GetEntityReqRes[V]]] = {
    Helpers
      .getResultFromPersistenceModule(param)
      .map(Helpers.convert(_))
   // todo-later - fix this return type - it makes absolutely zero sense
   //  there is too much "Option", it is "too" "safe" :)
  }

}
