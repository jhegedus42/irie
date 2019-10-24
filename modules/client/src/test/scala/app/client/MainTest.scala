package app.client

import app.shared.entity.asString.EntityWithRefAsJSON._
import io.circe.generic.auto._
import org.scalatest.FunSuite



class MainTest extends FunSuite {

  // this should be executed "normally" in a node+jsdom type of environment
  // but I tried it few years ago with karma and it worked too, however
  // for now, I am using node+jsdom environment because that integrates
  // well with sbt's testing system/commands/framework

  test( "simple synchronous (blocking) - 'integration test' stub" ) {
    println( "hello test world" )
  }

  test( "url_encoding_decoding" ) {

    import app.shared.initialization.testing.TestEntitiesForUsers._

    import scala.scalajs.js._

    val alice_as_json: String =
      fromEntityRef(aliceEntity).json.noSpaces
//      aliceEntity.toJSON().entityWithRefAsJSON.json.noSpaces

    val encoded: String = URIUtils.encodeURIComponent( alice_as_json )

    val decoded: String = URIUtils.decodeURIComponent( encoded )

    println(alice_as_json)
    println(encoded)
    println(decoded)

    assert( decoded === alice_as_json)

    println( s"Successfully completed without errors." )

  }


}
