package client.ui.login

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
