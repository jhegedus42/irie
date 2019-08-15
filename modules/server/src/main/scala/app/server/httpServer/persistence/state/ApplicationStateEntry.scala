package app.server.httpServer.persistence.state

case class ApplicationStateEntry(
    untypedRef:    UntypedRef,
    entityAsJSON: EntityAsJSON
)
