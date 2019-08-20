package app.server.httpServer.routes.dynamic.logic.typeClassInstances

import app.server.httpServer.routes.dynamic.logic.ServerSideLogic.ServerLogicTypeClass
import app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.PersistenceModule
import app.shared.comm.requests.GetEntityPostRequest
import app.shared.comm.requests.GetEntityPostRequest.{
  GetEntityRequestParameter,
  GetEntityRequestResult
}
import app.shared.entity.{Entity, RefToEntity}
import app.shared.entity.entityValue.EntityValue
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.Try

private[routes] object GetEntity {

  case class GetEntityLogic[V <: EntityValue[V]](
      persistenceModule: PersistenceModule,
      d:                 Decoder[Entity[V]],
      contextExecutor:   ExecutionContextExecutor,
  ) extends ServerLogicTypeClass[GetEntityPostRequest[V]] {

    override def getResult(
        param: GetEntityRequestParameter[V]
    ): Future[Option[GetEntityRequestResult[V]]] = {

      val p: RefToEntity[V] = param.par

      val res: Future[Option[Entity[V]]] =
        persistenceModule.getEntity[V]( p )( d )

      val r2: Future[GetEntityRequestResult[V]] =
        res.map( GetEntityRequestResult( _ ) )( contextExecutor )

      val r3: Future[Some[GetEntityRequestResult[V]]] =
        r2.map( Some( _ ) )( contextExecutor )

      r3.onComplete( (x: Try[Some[GetEntityRequestResult[V]]]) => {
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

}
