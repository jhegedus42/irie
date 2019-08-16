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

}
