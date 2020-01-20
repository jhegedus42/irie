package client.ui.helpers.login

import io.circe.{Decoder, Encoder}
import org.scalajs.dom.window
import org.scalajs.{dom => d}
import shared.dataStorage.model.User
import shared.dataStorage.relationalWrappers.TypedReferencedValue

import scala.scalajs.js

//import io.circe.generic.JsonCodec
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

//@Lenses

/**
  * Describes who is logged in, if anybody (None vs Some).
  *
  * @param userOption
  */

@JsonCodec
case class UserLoginStatus(
  userOption: Option[TypedReferencedValue[User]] = None) {}
