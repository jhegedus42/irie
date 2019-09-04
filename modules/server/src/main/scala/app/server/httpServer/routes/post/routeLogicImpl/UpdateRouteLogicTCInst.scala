package app.server.httpServer.routes.post.routeLogicImpl

import app.server.httpServer.routes.post.RouteLogicTypeClass
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.PersistentServiceProvider
import app.shared.comm.postRequests.UpdateEntityRoute
import app.shared.comm.postRequests.UpdateEntityRoute.{UpdateReqPar, UpdateReqRes}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag
import scala.util.Try

case class UpdateRouteLogicTCInst[V <: EntityValue[V]](
                                                  persistenceModule: PersistentServiceProvider,
                                                  d:                 Decoder[Entity[V]],
                                                  e_ent:             Encoder[Entity[V]],
                                                  ct:                ClassTag[V],
                                                  contextExecutor:   ExecutionContextExecutor
) extends RouteLogicTypeClass[UpdateEntityRoute[V]] {

  override def getResult(
      param: UpdateReqPar[V]
  ): Future[Option[UpdateReqRes[V]]] = {
//
//    val e: Entity[V] = param.entity
//
//    implicit val encoder: Encoder[Entity[V]] = e_ent
//    implicit val cti:     ClassTag[V]        = ct
//
//    val res: Future[( Entity[V] )] =
//      persistenceModule.updateEntity[V]( e )
//
//    val r2: Future[UpdateReqRes[V]] =
//      res.map( (x: ( Entity[V] )) => UpdateReqRes( x ) )(
//        contextExecutor
//      )
//
//    val r3: Future[Some[UpdateReqRes[V]]] =
//      r2.map( Some( _ ) )( contextExecutor )
//
//    r3.onComplete( (x: Try[Some[UpdateReqRes[V]]]) => {
//      val s2 =
//        s"""
//           |
//           |
//           |vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
//           |
//           |
//           |r3 has completed in `UpdateEntityLogic`
//           |it was called with param:
//           |$param
//           |
//           |it resulted in a :
//           |$x
//           |
//           |
//           |^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//           |
//           |
//           """.stripMargin
//      println( s2 )
//
//    } )( contextExecutor )
//
//    r3
    ???
  }

}