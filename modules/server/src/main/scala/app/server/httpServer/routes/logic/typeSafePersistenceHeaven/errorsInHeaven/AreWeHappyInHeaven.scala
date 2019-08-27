package app.server.httpServer.routes.logic.typeSafePersistenceHeaven.errorsInHeaven

sealed trait WeAreHappyInHeaven

sealed trait WeAreNotHappyInHeaven

case class AreWeHappyInTheUnderWorld(
    why: Either[WeAreNotHappyInHeaven, WeAreHappyInHeaven])
