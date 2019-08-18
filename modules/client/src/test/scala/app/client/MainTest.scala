package app.client

import org.scalatest.FunSuite

class MainTest extends FunSuite {

  // this should be executed "normally" in a node+jsdom type of environment
  // but I tried it few years ago with karma and it worked too, however
  // for now, I am using node+jsdom environment because that integrates
  // well with sbt's testing system/commands/framework

  test("simple synchronous (blocking) - 'integration test' stub"){
    println("hello test world")
    // todo-next create integration test for insert + get entity
    //  - insert a user
    //  - get the user
    //  - assert that the two are the same
    //    => we need to use async test for this (that is,
    //       we need to go back in time, way back, and see
    //       how the async test worked for the client a few
    //       hundred :) 'commits' ago :)
  }

  test("url encoding / decoding"){

    import java.net.{URLDecoder, URLEncoder}
    import scala.compat.Platform.currentTime

    object UrlCoded extends App {
      val original = """http://foo bar/"""
      val encoded: String = URLEncoder.encode(original, "UTF-8")

      assert(encoded == "http%3A%2F%2Ffoo+bar%2F", s"Original: $original not properly encoded: $encoded")

      val percentEncoding = encoded.replace("+", "%20")
      assert(percentEncoding == "http%3A%2F%2Ffoo%20bar%2F", s"Original: $original not properly percent-encoded: $percentEncoding")

      assert(URLDecoder.decode(encoded, "UTF-8") == URLDecoder.decode(percentEncoding, "UTF-8"))

      println(s"Successfully completed without errors. [total ${currentTime - executionStart} ms]")
    }

  }

}
