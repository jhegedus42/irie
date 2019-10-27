package app.client.ui.dom

import app.client.ui.components.mainPages.login.UserLoginStatus
import app.shared.entity.EntityWithRef
import app.shared.entity.asString.EntityWithRefAsJSON
import app.shared.entity.entityValue.values.User
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, Json}
import io.circe.parser._
import io.circe.{Decoder, Error, _}
import org.scalajs.dom.window
import monocle.macros.syntax.lens._
object Window {

  def setLoggedInUser(
    user: UserLoginStatus
  )(
    implicit enc: Encoder[UserLoginStatus]
  ): Unit = {

    println(s"user name is: "+user)

    val newName=s"${enc(user).noSpaces}"

    println(s"new window name is: "+newName)

    window.name = newName
  }

  def getUserLoginStatus(
  implicit dec:Decoder[UserLoginStatus]
  ):UserLoginStatus = {
    val s: String = window.name
    println(s"window name: $s")
    val ej: Option[UserLoginStatus] =decode(s).toOption
    val ej2: UserLoginStatus =ej.getOrElse(UserLoginStatus(None))
    println(s"win name: $s")
    println(s"decoded name: $ej")

    val x: UserLoginStatus =ej2
    println(s"login status:$x")

    Window.setLoggedInUser(x)

    x
  }

  def runWindowNameTest(): Unit = {

    import org.scalajs.dom.window

    val s: String = window.name

    var i = 0

    try {
      i = Integer.parseInt(s)
    } catch {
      case _ => i = 0
    }

    val i2 = i + 1
    val s2 = s"$i2"

    println(s"old window.dom = ${window.name}")

    window.name = s"$s2"

    println(s"new window.dom = ${window.name}")
  }

}
