package app.server.httpServer.persistence.persistentActor.state

import app.shared.dataModel.value.EntityAsJSON

case class ApplicationStateEntry(
    untypedRef:    UntypedRef,
    entityAsJSON: EntityAsJSON
)
