package app.server.httpServer.routes.post.requestHandler.handlers.persistence

sealed trait OperationType

trait Get
trait Insert
trait Update

sealed trait Typesafeness
trait TypesafeOperation
trait NotTypesafeOperation
