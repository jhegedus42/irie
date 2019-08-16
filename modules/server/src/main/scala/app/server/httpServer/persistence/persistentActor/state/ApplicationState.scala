package app.server.httpServer.persistence.persistentActor.state

import app.shared.dataModel.value.EntityValue

import scala.reflect.ClassTag

case class ApplicationState(
    val map: Map[UntypedRef, ApplicationStateEntry] = Map.empty
)
