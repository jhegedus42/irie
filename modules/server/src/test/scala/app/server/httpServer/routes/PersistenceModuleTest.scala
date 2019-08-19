package app.server.httpServer.routes

import akka.actor.ActorSystem
import app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.PersistenceModule
import app.server.httpServer.routes.dynamic.logic.typeClassInstances.persistence.persistentActor.state.{StateChange, StatePrintingUtils}
import app.server.utils.PrettyPrint
import app.shared.entity.Entity
import app.shared.entity.entityValue.values.User
import app.shared.utils.macros.compilationTime.AppendCompilationTimeToString
import org.scalatest.FunSuite
import scala.language.postfixOps

import scala.concurrent.Future



class PersistenceModuleTest extends FunSuite {

  test( "testInit" ) {
    println( s"\n\nThis is where the Persistence Module will be tested.\n\n" )

  }

  @AppendCompilationTimeToString
  val compilationTime: String = ""

  test( "compilation time macro test" ) {

    println(
      s"\n\n" +
        s"Compilation time for " +
        s"PersistenceModuleTest was : $compilationTime\n\n"
    )

  }

  test( "pretty print Alice" ) {
    val alice        = User( "Alice", 38 )
    val pretty_alice = PrettyPrint.prettyPrint( alice )
    print(
      s"\n\nalice pretty printed at compilation time of '$compilationTime' is :\n$pretty_alice\n\n"
    )
  }

  test( "init" ) {

    def init(): Unit = {
      import app.shared.dataModel.testUsers.TestUsers._
      import io.circe.generic.auto._

      println( "--------------\n\nHere STARTS the init:\n\n" )

      val actorSystem: ActorSystem =
        ActorSystem( "ActorSystem_for_all_Actors_in_the_app" )
      val pm = PersistenceModule( actorSystem )
      val res: Future[( StateChange, Entity[User] )] =
        pm.createAndStoreNewEntity( cica )
      import scala.concurrent._
      import scala.concurrent.duration._
      val res_awaited: ( StateChange, Entity[User] ) =
        Await.result( res, 5 seconds )
      val stateChange = res_awaited._1

      StatePrintingUtils.printStateChange( stateChange )

      println( "--------------\n\nHere ENDS the init.\n\n" )

    }

    init()

  }

}
