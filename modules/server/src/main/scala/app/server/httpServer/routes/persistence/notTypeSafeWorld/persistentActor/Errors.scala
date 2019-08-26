package app.server.httpServer.routes.persistence.notTypeSafeWorld.persistentActor

import app.server.httpServer.routes.persistence.notTypeSafeWorld.ApplicationStateMapEntry
import app.server.httpServer.routes.persistence.notTypeSafeWorld.state.UntypedRef

object Errors {
  case class EntityUpdateVersionError(
      utr:                      UntypedRef,
      applicationStateMapEntry: ApplicationStateMapEntry
  )

}
