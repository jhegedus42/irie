package app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor

import app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor.state.ApplicationStateMapEntry
import app.server.httpServer.routes.post.requestHandler.handlers.persistence.actor.state.refs.UntypedRef

sealed trait PersistentActorError

case class EntityUpdateVersionError (
  utr:                      UntypedRef,
  applicationStateMapEntry: ApplicationStateMapEntry
) extends PersistentActorError
