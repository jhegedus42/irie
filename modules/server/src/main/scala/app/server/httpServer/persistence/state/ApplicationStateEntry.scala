package app.server.httpServer.persistence.state

import app.shared.dataModel.value.EntityAsJSON

case class ApplicationStateEntry(
    untypedRef:    UntypedRef,
    entityAsJSON: EntityAsJSON
)
