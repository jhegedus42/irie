package app.shared.initialization.testing

import io.circe.generic.auto._
import app.shared.state.ApplicationStateMap

case class TestApplicationState ( applicationStateMap: ApplicationStateMap)

object TestApplicationState {
  def getTestState:TestApplicationState ={
    val init=ApplicationStateMap()
    val withAlice=init.insertEntity(TestUsers.aliceEntity)
    val withBob=withAlice.insertEntity(TestUsers.bobEntity)
    TestApplicationState(withBob)
  }

}

