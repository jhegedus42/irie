package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence

import app.shared.entity.entityValue.EntityValue


    trait EntityParameter[V <: EntityValue[V], C <: Command]
        extends Parameter[C]

  sealed trait PersistenceError[C <: Command]

  sealed trait Result[C <: Command] {
//  def getResult : Either[PersistenceError[C]]
  }

