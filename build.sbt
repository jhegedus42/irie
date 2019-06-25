import org.scalajs.core.tools.io.{MemVirtualJSFile, VirtualJSFile}
name := "IM root project"

import sbt.Keys._
import sbt.Project.projectToRef
//resolvers += Resolver.bintrayRepo( "johnreed2", "maven" )
//resolvers += Resolver.sonatypeRepo("releases")
lazy val macroVersion = "2.1.1"

lazy val paradisePlugin = Def.setting{
    Seq(compilerPlugin("org.scalamacros" % "paradise" % macroVersion cross CrossVersion.patch))}

// a special crossProject for configuring a JS/JVM/shared structure
lazy val layer_Z_JVM_and_JS_shared =
  (crossProject.crossType( CrossType.Pure ) in file( "layer_Z_JVM_and_JS_shared" ))
    .settings(
      scalaVersion := Settings.versions.scala,
      logLevel := Level.Error,
      libraryDependencies ++= Settings.sharedDependencies.value,
      libraryDependencies ++= paradisePlugin.value
)

lazy val layer_Z_JVM_shared = layer_Z_JVM_and_JS_shared.jvm.settings( name := "layer_Z_JVM_shared" )

lazy val layer_Z_JS_shared = layer_Z_JVM_and_JS_shared.js.settings( name := "layer_Z_JS_shared" )



lazy val myEnv= new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv() {
  val jsChangeURLCode = {
    s"""
       |(function () {
       |  console.log('42 137') 
       |})();
       |""".stripMargin
  }
  val file = Seq(new MemVirtualJSFile("init_change_URL.js").withContent(jsChangeURLCode))
  override def customInitFiles(): Seq[VirtualJSFile] = file // super.customInitFiles()
}

// instantiate the JS project for SBT with some additional settings
lazy val layer_V_JS_client: Project = (project in file( "layer_V_JS_client" ))
  .settings(
    name := "layer_V_JS_client",
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    libraryDependencies ++= Settings.scalajsDependencies.value,
    parallelExecution in Test := false,
    logLevel := Level.Error,
    mainClass in Compile := Some( "app.client.Main" ),
//    jsEnv := new JSDOMNodeJSEnv2(), // this is a hack to make testing on node.js possible
      // 629C403A-E26F-46FD-92AD-11C47E7BB90B 
      jsEnv := myEnv,

        scalaJSOptimizerOptions ~= { _.withDisableOptimizer( true ) }
  )
  .enablePlugins( ScalaJSPlugin )
//  .dependsOn( layer_Z_JS_shared % "compile->compile;test->test" )
  .dependsOn( layer_Z_JS_shared % "compile->compile" )

// Client projects (just one in this case)
lazy val clients = Seq( layer_V_JS_client )

lazy val layer_Y_JVM_persistence = (project in file( "layer_Y_JVM_persistence" ))
  .settings(
    name := "layer_Y_JVM_persistence",
    version := Settings.version,
    logLevel := Level.Error,
    libraryDependencies ++= Settings.jvmDependencies.value,
    scalaVersion := Settings.versions.scala
  ).dependsOn( layer_Z_JVM_shared )

lazy val layer_X_JVM_stateAccess = (project in file( "layer_X_JVM_stateAccess" ))
  .settings(
    name := "layer_X_JVM_stateAccess",
    version := Settings.version,
    logLevel := Level.Error,
    libraryDependencies ++= Settings.jvmDependencies.value,
    scalaVersion := Settings.versions.scala
  ).dependsOn( layer_Y_JVM_persistence % "compile->compile;test->test" )

// instantiate the JVM project for SBT with some additional settings
lazy val layer_W_JVM_akka_http_server = (project in file( "layer_W_JVM_akka_http_server" ))
  .settings(
    name := "layer_W_JVM_akka_http_server",
    version := Settings.version,
    logLevel := Level.Error,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= Settings.scalacOptions,
    libraryDependencies ++= Settings.jvmDependencies.value,
    mainClass in Test := Some( "app.server.rest.testServers.TestServer_App_Basic_Data" ),
    mainClass in Compile := Some( "app.server.rest.TestHttpServerApp" ) //,
  )
  .dependsOn( layer_Z_JVM_shared % "compile->compile;test->test" )
  .dependsOn( layer_X_JVM_stateAccess % "compile->compile;test->test" )

logBuffered in Test := false

scalaJSUseMainModuleInitializer in Compile := true

cancelable in Global := true
logLevel := Level.Error
