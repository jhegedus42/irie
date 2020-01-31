package app.server.httpServer.authentication
//import com.outr.hasher.Implicits._

import com.roundeights.hasher.Implicits._
import scala.language.postfixOps


object HashUtils {
  def getSHA1(pwd:String):String={
    pwd.sha1.hex
  }

}
