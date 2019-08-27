package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.persistentActor.commands.state.refs.UntypedRef

sealed trait PersistentActorError

case class EntityUpdateVersionError(
//    utr:                      UntypedRef,
//    applicationStateMapEntry: ApplicationStateMapEntry)
    utr:                      UntypedRef
                                   )
    extends PersistentActorError
