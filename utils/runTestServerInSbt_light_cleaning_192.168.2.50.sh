sbt 'clean'
sbt 'compile'
sbt 'fastOptJS'
sbt 'layer_W_JVM_akka_http_server/test:runMain app.server.RESTService.mocks.runnableApps.TestServer_App_LabelOne_ClientTesting "192.168.2.50"'
