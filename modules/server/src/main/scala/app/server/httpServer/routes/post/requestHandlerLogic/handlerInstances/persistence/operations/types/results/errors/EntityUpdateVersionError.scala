package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.operations.types.results.errors

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor.commands.state.refs.UntypedRef



case class EntityUpdateVersionError(
//    utr:                      UntypedRef,
//    applicationStateMapEntry: ApplicationStateMapEntry)
    utr:                      UntypedRef
                                   )
    extends PersistentActorError
