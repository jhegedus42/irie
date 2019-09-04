package app.server.httpServer.routes.post.routeLogicImpl

import app.server.httpServer.routes.post.RouteLogicTypeClass
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.PersistentServiceProvider
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.Insert
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.persistenceOperations.crudOps.Insert.InsertOp
import app.shared.comm.postRequests.InsertNewEntityRoute
import app.shared.comm.postRequests.InsertNewEntityRoute.{
  InsertReqPar,
  InsertReqRes
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag
import scala.util.Try

case class InsertRouteLogicTCInst[V <: EntityValue[V]](
    persistenceModule: PersistentServiceProvider,
    d:                 Decoder[Entity[V]],
    e:                 Encoder[Entity[V]],
    ct:                ClassTag[V],
    contextExecutor:   ExecutionContextExecutor
) extends RouteLogicTypeClass[InsertNewEntityRoute[V]] {

  override def getResult(
      param: InsertReqPar[V]
  ): Future[Option[InsertReqRes[V]]] = {

    implicit val i1: Insert.InsertPersistenceOperationExecutorTypeClassImpl[V] =
      Insert.InsertPersistenceOperationExecutorTypeClassImpl(d, e,ct)
    val iop: Insert.InsertOpPar[V] = Insert.InsertOpPar(param.value)
    val res: Future[Insert.InsertOpRes[V]] =
      persistenceModule.executePersistenceOperation[Insert.InsertOp[V]](iop)

    def convert(ior: Insert.InsertOpRes[V]): Option[InsertReqRes[V]] =
       ior.res.toOption.map(InsertReqRes(_))

    val toReturn: Future[Option[InsertReqRes[V]]] =
      res.map(convert(_))(contextExecutor)
    toReturn
  }

}

//
//    val p: V = param.value
//
//    implicit val encoder: Encoder[Entity[V]] = e
//    implicit val cti:     ClassTag[V]        = ct
//
//    val res: Future[Entity[V]] =
//      persistenceModule.createAndStoreNewEntity[V]( p )
//
//    val r2: Future[InsertReqRes[V]] =
//      res.map( (x: Entity[V]) => InsertReqRes( x ) )(
//        contextExecutor
//      )
//
//    val r3: Future[Some[InsertReqRes[V]]] =
//      r2.map( Some( _ ) )( contextExecutor )
//
//    r3.onComplete( (x: Try[Some[InsertReqRes[V]]]) => {
//      val s2 =
//        s"""
//            |
//            |
//            |vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
//            |
//            |
//            |r3 has completed in `InsertEntityLogic`
//            |it was called with param:
//            |$param
//            |
//            |it resulted in a :
//            |$x
//            |
//            |
//            |^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//            |
//            |
//           """.stripMargin
//      println( s2 )
//
//    } )( contextExecutor )
//
//    r3
/// ??? // todo-later
