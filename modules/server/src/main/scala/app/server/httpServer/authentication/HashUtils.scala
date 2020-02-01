package app.server.httpServer.authentication
//import com.outr.hasher.Implicits._

import com.roundeights.hasher.Implicits._
import shared.dataStorage.model.{PWDHashed, PWDNotHashed}

import scala.language.postfixOps


object HashUtils {
  def getSHA1(pwdNotHashed:PWDNotHashed):PWDHashed={
    PWDHashed(pwdNotHashed.str.sha1.hex)
  }

}
