import sbt.Def

name := "root"

import sbt.Keys._

ThisBuild / resolvers += Resolver.jcenterRepo
ThisBuild / resolvers += Resolver.JCenterRepository
ThisBuild / resolvers += Resolver.bintrayRepo("naftoligug", "maven")
ThisBuild / resolvers += "jitpack" at "https://jitpack.io"

lazy val macroVersion = "2.1.1"

lazy val paradisePlugin: Def.Initialize[Seq[ModuleID]] = Def.setting {
  Seq(
    compilerPlugin(
      "org.scalamacros" % "paradise" % macroVersion cross CrossVersion.patch
    )
  )
}

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
)

lazy val shared =
  (crossProject.crossType(CrossType.Pure) in file(
    "modules/shared"
  )).settings(
    name := "shared",
    scalaVersion := Settings.versions.scala,
    libraryDependencies ++= Settings.sharedDependencies.value,
    libraryDependencies ++= paradisePlugin.value
  )

lazy val shared_jvm =
  shared.jvm.settings(name := "shared_jvm")

lazy val shared_js =
  shared.js.settings(name := "shared_js")

lazy val client: Project = (project in file("modules/client"))
  .settings(
    name := "client",
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= Settings.scalacOptions,
    libraryDependencies ++= Settings.scalajsDependencies.value,
    libraryDependencies ++= paradisePlugin.value,
    jsEnv := new CustomJSDOMNODEJsEnv(), // this is a hack to make testing on node.js possible
//    scalaJSModuleKind := ModuleKind.CommonJSModule,
    scalaJSOptimizerOptions ~= {
      _.withDisableOptimizer(true)
    },
    scalaJSLinkerConfig ~= {
      _.withOptimizer(false)
    }
  ) // see this page for details : https://www.scala-js.org/doc/project/building.html#disabling-the-optimizations
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(shared_js % "compile->compile;test->test")

lazy val server =
  (project in file("modules/server"))
    .settings(
      name := "server",
      version := Settings.version,
      scalaVersion := Settings.versions.scala,
      libraryDependencies ++= paradisePlugin.value,
      scalacOptions ++= Settings.scalacOptions,
      libraryDependencies ++= Settings.jvmDependencies.value
    )
    .dependsOn(shared_jvm % "compile->compile;test->test")

logBuffered in Test := false

scalaJSUseMainModuleInitializer in Compile := true

cancelable in Global := true
logLevel := Level.Error
