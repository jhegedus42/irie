package app.server.httpServer.routes.routeProviders.dynamicRouteProviders.serverLogicAsTypeClasses.instanceFactories

import app.server.httpServer.routes.persistenceProvider.PersistenceModule
import app.server.httpServer.routes.persistenceProvider.persistentActor.state.StateChange
import app.server.httpServer.routes.routeProviders.dynamicRouteProviders.serverLogicAsTypeClasses.ServerLogicTypeClass
import app.shared.comm.postRequests.{InsertNewEntityReq, UpdateEntityReq}
import app.shared.comm.postRequests.InsertNewEntityReq.{InsertReqPar, InsertReqRes}
import app.shared.comm.postRequests.UpdateEntityReq.{UpdateReqPar, UpdateReqRes}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag
import scala.util.Try

case class UpdateEntityLogic[V <: EntityValue[V]](
    persistenceModule: PersistenceModule,
    d:                 Decoder[Entity[V]],
    e_ent:             Encoder[Entity[V]],
    ct:                ClassTag[V],
    contextExecutor:   ExecutionContextExecutor
) extends ServerLogicTypeClass[UpdateEntityReq[V]] {

  override def getResult(
      param: UpdateReqPar[V]
  ): Future[Option[UpdateReqRes[V]]] = {

    val e: Entity[V] = param.entity

//    implicit val encoder: Encoder[Entity[V]] = e
    implicit val cti:     ClassTag[V]        = ct

//    val res = persistenceModule.createAndStoreNewEntity[V]( p )
    val res: Future[(StateChange, Entity[V])] = persistenceModule.updateEntity[V]( e )

    val r2: Future[UpdateReqRes[V]] =
      res.map( (x: ( StateChange, Entity[V] )) => UpdateReqRes( x._2 ) )( contextExecutor )

    val r3: Future[Some[UpdateReqRes[V]]] =
      r2.map( Some( _ ) )( contextExecutor )

    r3.onComplete( (x: Try[Some[UpdateReqRes[V]]]) => {
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
