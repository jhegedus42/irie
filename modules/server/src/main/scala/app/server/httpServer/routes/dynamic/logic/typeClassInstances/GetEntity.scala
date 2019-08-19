package app.server.httpServer.routes.dynamic.logic.typeClassInstances

import app.server.httpServer.routes.dynamic.logic.ServerSideLogic.ServerLogicTypeClass
import app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.PersistenceModule
import app.shared.comm.requests.GetEntityRequest
import app.shared.comm.requests.GetEntityRequest.{
  GetEntityRequestParameter,
  GetEntityRequestResult
}
import app.shared.entity.{Entity, RefToEntity}
import app.shared.entity.entityValue.EntityValue
import io.circe.Decoder

import scala.concurrent.{ExecutionContextExecutor, Future}

private[routes] object GetEntity {

  case class GetEntityLogic[V <: EntityValue[V]](
      persistenceModule: PersistenceModule,
      d:                 Decoder[Entity[V]],
      contextExecutor:   ExecutionContextExecutor,
  ) extends ServerLogicTypeClass[GetEntityRequest[V]] {

    override def getResult(
        param: GetEntityRequestParameter[V]
    ): Future[Option[GetEntityRequestResult[V]]] = {

      val p: RefToEntity[V] = param.par
      val res: Future[Option[Entity[V]]] =
        persistenceModule.getEntity[V]( p )( d )

      val r2: Future[GetEntityRequestResult[V]] =
        res.map( GetEntityRequestResult( _ ) )( contextExecutor )

      r2.map( Some( _ ) )( contextExecutor )

    }

  }

}
