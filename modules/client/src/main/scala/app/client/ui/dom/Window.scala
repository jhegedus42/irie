package app.client.ui.dom

import app.client.ui.components.mainPages.login.LoginPageComp.State.UserLoginStatus
import app.shared.entity.EntityWithRef
import app.shared.entity.asString.EntityWithRefAsJSON
import app.shared.entity.entityValue.values.User
import app.shared.utils.UUID_Utils.EntityIdentity
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, Json}
import io.circe.parser._
import io.circe.{Decoder, Error, _}
import org.scalajs.dom.window

object Window {

  def setLoggedInUser(
    user: Option[EntityWithRef[User]]
  )(
    implicit enc: Encoder[Option[EntityWithRef[User]]]
  ): Unit = {

    val newName=s"${enc(user).noSpaces}"

    println(s"new window name is: "+newName)

    window.name = newName
  }

  def getUserLoginStatus(
  implicit dec:Decoder[Option[EntityWithRef[User]]]
  ):UserLoginStatus = {
    val s: String = window.name
    println(s"window name: $s")
    val ej: Either[Error, Option[EntityWithRef[User]]] =decode(s)
    println(s"win name: $s")
    println(s"decoded name: $ej")

    val res1=ej.toOption.flatten
    println(s"login status:$res1")
    val x: UserLoginStatus =UserLoginStatus(res1)
    Window.setLoggedInUser(Some(x.userOption.get.copy()))
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
