package app.server.RESTService.mocks.runnableApps

import app.server.RESTService.mocks.TestServerFactory
import app.testHelpersServer.state.TestData
import app.testHelpersShared.data.TestDataLabels.LabelOne

object TestServer_App_LabelOne_ClientTesting extends App {
  println("before iszunk a medve borere")
  TestServerFactory
  .getTestServer(TestData.getTestDataFromLabels(LabelOne))
  .start(args)
}
