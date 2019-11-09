package app.server.httpServer.routes.sodium

import app.server.httpServer.routes.persistentActor.PersistentActorWhisperer
import app.shared.entity.entityValue.EntityType

case class SodiumRouteFactory[V<:EntityType[V]](paw:PersistentActorWhisperer) {


}

