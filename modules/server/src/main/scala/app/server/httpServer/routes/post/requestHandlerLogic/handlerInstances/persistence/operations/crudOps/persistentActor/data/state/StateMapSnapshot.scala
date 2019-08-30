package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.crudOps.persistentActor.data.state

private[persistentActor] case class StateMapSnapshot(
    val map: Map[UntypedRef, StateMapEntry] = Map.empty
)
