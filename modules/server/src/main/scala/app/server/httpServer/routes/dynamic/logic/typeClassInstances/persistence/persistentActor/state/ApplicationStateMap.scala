package app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.persistentActor.state

case class ApplicationStateMap(
    val map: Map[UntypedRef, ApplicationStateMapEntry] = Map.empty
)
