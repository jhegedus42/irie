package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances

import app.server.httpServer.routes.post.requestHandlerLogic.RequestHandlerTC
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.PersistenceService
import app.shared.comm.postRequests.GetEntityReq
import app.shared.comm.postRequests.GetEntityReq.{GetEntityReqPar, GetEntityReqRes}
import app.shared.entity.entityValue.EntityValue
import app.shared.entity.Entity
import app.shared.entity.refs.{RefToEntityWithVersion, RefToEntityWithoutVersion}
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.Try

case class GetEntityHandler[V <: EntityValue[V]](
                                                  persistenceModule: PersistenceService,
                                                  d:                 Decoder[Entity[V]],
                                                  contextExecutor:   ExecutionContextExecutor
) extends RequestHandlerTC[GetEntityReq[V]] {

  override def getResult(
      param: GetEntityReqPar[V]
  ): Future[Option[GetEntityReqRes[V]]] = {

    val p: RefToEntityWithoutVersion[V] = param.refToEntityWithoutVersion

    val res: Future[Option[Entity[V]]] =
      persistenceModule.getEntityWithLatestVersion[V]( p )( d ) //todo-right-now

    val r2: Future[GetEntityReqRes[V]] =
      res.map( GetEntityReqRes( _ ) )( contextExecutor )

    val r3: Future[Some[GetEntityReqRes[V]]] =
      r2.map( Some( _ ) )( contextExecutor )

    r3.onComplete( (x: Try[Some[GetEntityReqRes[V]]]) => {
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
      println( s2 )

    } )( contextExecutor )

    r3

  }

}
