package app.server.httpServer.routes.persistence.typeSafeWorld.errors

sealed trait WeAreHappy

sealed trait WeAreNotHappy

case class AreWeHappy[V](
    why:                        Either[WeAreNotHappy, WeAreHappy],
    ifWeAreHappyWeHaveAPresent: Option[V])
