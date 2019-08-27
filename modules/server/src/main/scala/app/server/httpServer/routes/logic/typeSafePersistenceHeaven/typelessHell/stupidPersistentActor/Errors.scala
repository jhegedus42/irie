package app.server.httpServer.routes.logic.typeSafePersistenceHeaven.typelessHell.stupidPersistentActor

import app.server.httpServer.routes.logic.typeSafePersistenceHeaven.typelessHell.state.ApplicationStateMapEntry
import app.server.httpServer.routes.logic.typeSafePersistenceHeaven.typelessHell.state.refs.UntypedRef

object Errors {
  case class EntityUpdateVersionError(
      utr:                      UntypedRef,
      applicationStateMapEntry: ApplicationStateMapEntry
  )

}
