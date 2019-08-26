package app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.askWorld.persistentActor

import app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.state.ApplicationStateMapEntry
import app.server.httpServer.routes.persistence.typeSafeWorld.styx.typelessUnderWorld.state.refs.UntypedRef

object Errors {
  case class EntityUpdateVersionError(
      utr:                      UntypedRef,
      applicationStateMapEntry: ApplicationStateMapEntry
  )

}
