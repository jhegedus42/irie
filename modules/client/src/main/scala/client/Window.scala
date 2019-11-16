package client

import dataStorage.{ReferencedValue, User}
import io.circe.{Decoder, Encoder}
import org.scalajs.dom.window
import org.scalajs.{dom => d}

import scala.scalajs.js

//import io.circe.generic.JsonCodec
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

//@Lenses

@JsonCodec
case class UserLoginStatus(
  userOption: Option[ReferencedValue[User]] = None) {}

object Router {
  // https://stackoverflow.com/questions/25806608/how-to-detect-browser-back-button-event-cross-browser
  // https://www.scala-js.org/doc/interoperability/types.html

  def disableBackButton(): Unit = {
    js.Dynamic.literal("page" -> 1)
    val l = js.Dynamic.literal
    val l1: js.Object with js.Dynamic = l("page" -> 1)
    d.window.history.pushState(l1, """, """)

    d.window.onpopstate = {
      a =>

        println(a)
        d.window.history.go(1)
    }
  }
}

object Window {

  def setLoggedInUser(
    user: UserLoginStatus
  )(
    implicit
    enc: Encoder[UserLoginStatus]
  ): Unit = {

    println(s"user name is: " + user)

    val newName = s"${enc(user).noSpaces}"

    println(s"new window name is: " + newName)

    window.name = newName
  }

  def getUserLoginStatus(
    implicit
    dec: Decoder[UserLoginStatus]
  ): UserLoginStatus = {
    val s: String = window.name
    println(s"window name: $s")
    val ej:  Option[UserLoginStatus] = decode(s).toOption
    val ej2: UserLoginStatus         = ej.getOrElse(UserLoginStatus(None))
    println(s"win name: $s")
    println(s"decoded name: $ej")

    val x: UserLoginStatus = ej2
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
