package app.server.httpServer.routes.persistenceProvider.persistentActor

import app.shared.state.{ApplicationStateMapEntry, UntypedRef}

object Errors {
  case class EntityUpdateVersionError(
      utr:                      UntypedRef,
      applicationStateMapEntry: ApplicationStateMapEntry
  )

}
