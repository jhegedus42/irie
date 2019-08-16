package app.server.httpServer.persistence.persistentActor.state

import app.shared.dataModel.value.EntityValue

import scala.reflect.ClassTag

case class ApplicationStateMap(
    val map: Map[UntypedRef, ApplicationStateMapEntry] = Map.empty
)