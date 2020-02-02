package app.server

import akka.actor.ActorSystem
import app.server.httpServer.HttpServer
import com.typesafe.config.ConfigFactory
import shared.communication.persActorCommands.auth.QueryAuthWrapper
import shared.communication.persActorCommands.crudCMDs.GetAllEntityiesForUserPersActCmd
import shared.testingData.TestDataStore

object Main extends App {

  val ctest = GetAllEntityiesForUserPersActCmd(
    TestDataStore.aliceUserEnt.ref.unTypedRef.refToEntityOwningUser,
    None
  )

  import io.circe.parser._
  import io.circe.syntax._

//  val res = decode[QueryAuthWrapper[GetAllEntityiesForUserPersActCmd]](s)


  implicit lazy val app = ActorSystem(
    "App"
  )

  val server = HttpServer(app)

  val host: String = if (args.length == 0) "localhost" else args(0)

  server.startServer(host)

}
