package app.server.persistence.state

case class ApplicationStateEntry(
    untypedRef:    UntypedRef,
    entityAsJSON: EntityAsJSON
)
