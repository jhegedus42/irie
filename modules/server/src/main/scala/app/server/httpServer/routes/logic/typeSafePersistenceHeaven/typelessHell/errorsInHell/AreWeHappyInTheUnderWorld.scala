package app.server.httpServer.routes.logic.typeSafePersistenceHeaven.typelessHell.errorsInHell

sealed trait WeAreHappyInTheUnderWorld

sealed trait WeAreNotHappyInTheUnderWorld

case class AreWeHappyInTheUnderWorld(
    why: Either[WeAreNotHappyInTheUnderWorld, WeAreHappyInTheUnderWorld])
