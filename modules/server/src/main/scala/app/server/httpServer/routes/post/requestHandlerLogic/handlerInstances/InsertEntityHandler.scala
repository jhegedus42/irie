package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances

import app.server.httpServer.routes.post.requestHandlerLogic.Logic
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.PersistenceService
import app.shared.comm.postRequests.InsertNewEntityReq
import app.shared.comm.postRequests.InsertNewEntityReq.{
  InsertReqPar,
  InsertReqRes
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag
import scala.util.Try

case class InsertEntityHandler[V <: EntityValue[V]](
  persistenceModule: PersistenceService,
  d:                 Decoder[Entity[V]],
  e:                 Encoder[Entity[V]],
  ct:                ClassTag[V],
  contextExecutor:   ExecutionContextExecutor)
    extends Logic[InsertNewEntityReq[V]] {

  override def getResult(
    param: InsertReqPar[V]
  ): Future[Option[InsertReqRes[V]]] = {

    val p: V = param.value

    implicit val encoder: Encoder[Entity[V]] = e
    implicit val cti:     ClassTag[V]        = ct

    val res: Future[Entity[V]] =
      persistenceModule.createAndStoreNewEntity[V]( p )

    val r2: Future[InsertReqRes[V]] =
      res.map( (x: Entity[V]) => InsertReqRes( x ) )(
        contextExecutor
      )

    val r3: Future[Some[InsertReqRes[V]]] =
      r2.map( Some( _ ) )( contextExecutor )

    r3.onComplete( (x: Try[Some[InsertReqRes[V]]]) => {
      val s2 =
        s"""
            |
            |
            |vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
            |
            |
            |r3 has completed in `InsertEntityLogic`
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
