package app.server

import io.circe.generic.JsonCodec

import scala.io.{BufferedSource, Source}

case class Config(
  val port:         Int     = 8080,
  val areWeTesting: Boolean = true)

@JsonCodec
case class ConfigFromJSON(
                           port: Int,
                           ipForClient:     String,
                           ipForServer:String)

object Config {

  lazy val configFromJSON: ConfigFromJSON = {
    val fileSource: BufferedSource = Source.fromFile("config.json")
    val configAsString = fileSource.mkString

    println(configAsString)

    import io.circe.Decoder
    import io.circe._
    import io.circe.generic.JsonCodec
    import io.circe.generic.auto._
    import io.circe.parser._
    import io.circe.syntax._

    val config = decode[ConfigFromJSON](configAsString).toOption.head

    config
  }

//  def getDefaultConfig = Config()
}
