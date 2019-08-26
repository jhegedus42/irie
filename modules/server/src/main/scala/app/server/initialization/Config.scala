package app.server.initialization

case class Config(
  val port : Int = 8080,
  val areWeTesting: Boolean = true
)

object Config {
  def getDefaultConfig = Config()
}
