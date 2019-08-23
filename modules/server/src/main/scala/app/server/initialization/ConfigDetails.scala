package app.server.initialization

/**
  * Created by joco on 16/06/2017.
  */
case class ConfigDetails(
    areWeTesting: Boolean,
    testApplicationState: TestApplicationState =
      TestApplicationState.getTestState
)
