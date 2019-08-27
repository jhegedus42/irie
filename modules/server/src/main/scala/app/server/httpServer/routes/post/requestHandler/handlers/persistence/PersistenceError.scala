package app.server.httpServer.routes.post.requestHandler.handlers.persistence

sealed trait PersistenceError[OT<:OperationType]

