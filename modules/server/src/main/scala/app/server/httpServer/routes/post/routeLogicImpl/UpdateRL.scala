package app.server.httpServer.routes.post.routeLogicImpl

import app.server.httpServer.routes.post.RouteLogic
import app.server.httpServer.routes.post.routeLogicImpl.persistenceService.PersistentServiceProvider
import app.shared.comm.postRequests.UpdateEntityRoute
import app.shared.comm.postRequests.UpdateEntityRoute.{
  UpdateReqPar,
  UpdateReqRes
}
import app.shared.entity.Entity
import app.shared.entity.entityValue.EntityValue
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.reflect.ClassTag
import scala.util.Try

/**
  *
  *
  * Type class instance.
  * Calculates / executes what should happen when the update "REST endpoint" is "called".
  *
  *
  * @param persistenceModule
  * @param d
  * @param e_ent
  * @param ct
  * @param contextExecutor
  * @tparam V
  */
case class UpdateRL[V <: EntityValue[V]](
    persistenceModule: PersistentServiceProvider,
    d:                 Decoder[Entity[V]],
    e_ent:             Encoder[Entity[V]],
    ct:                ClassTag[V],
    contextExecutor:   ExecutionContextExecutor
) extends RouteLogic[UpdateEntityRoute[V]] {

  override def getHttpReqResult(
      param: UpdateReqPar[V]
  ): Future[Option[UpdateReqRes[V]]] = {

    //todo-now-1 implement this
    // get inspiration from : InsertRL[]().getResult(...)

    // which goes like this:

    /*
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
     */

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
