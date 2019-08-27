package app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.actor

import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.actor.state.ApplicationStateMapEntry
import app.server.httpServer.routes.post.requestHandlerLogic.handlerInstances.persistence.actor.state.refs.UntypedRef

sealed trait PersistentActorError

case class EntityUpdateVersionError(
    utr:                      UntypedRef,
    applicationStateMapEntry: ApplicationStateMapEntry)
    extends PersistentActorError
