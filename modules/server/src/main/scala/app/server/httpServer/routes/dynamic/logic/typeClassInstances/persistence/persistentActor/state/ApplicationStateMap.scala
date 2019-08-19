package app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.persistentActor.state

import app.shared.entity.entityValue.EntityValue

import scala.reflect.ClassTag

case class ApplicationStateMap(
    val map: Map[UntypedRef, ApplicationStateMapEntry] = Map.empty
)
