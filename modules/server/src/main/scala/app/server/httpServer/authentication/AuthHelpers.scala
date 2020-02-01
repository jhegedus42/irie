package app.server.httpServer.authentication

import akka.actor.ActorRef
import shared.dataStorage.model.{PWDHashed, PWDNotHashed}

object AuthHelpers {

  def auth(
    PWDNotHashed: PWDNotHashed,
    sender:       ActorRef
  )(pwdHashed:    PWDHashed
  )(f:            => Unit
  ): Unit = {
    ???

    if(HashUtils.getSHA1(PWDNotHashed)==pwdHashed){

    }else(
      ???
    )

  }

}
