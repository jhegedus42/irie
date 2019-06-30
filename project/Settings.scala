//import org.scalajs.jsenv.ExternalJSEnv.{AsyncExtRunner, ExtRunner}
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._



import java.io.{Console => _, _}





object Settings {

//  val ex=exclude("com.chuusai","shapeless")
  /** The name of your application */
  /** The version of your application */
  val version = "1.1.4"

//  val targetDir = (classDirectory in Compile).value / "web"

  /** Options for the scala compiler */
  val scalacOptions = Seq(
    "-Xlint",
    "-unchecked",
    "-deprecation",
    "-feature",
    "-Yrangepos" //,
//    "-Ylog-classpath"
//    "-Xlog-implicits"
  )

  /** Declare global dependency versions here to avoid mismatches in multi part dependencies */
  object versions {
    val scala        = "2.12.6"
    val scalaDom     = "0.9.6"
    val scalajsReact = "1.3.1"
    val scalaCSS     = "0.5.5"
    val scalaZ       = "7.2.26"

    val akka              = "2.5.1"
    val in_memory_journal = "2.5.1.1"

    val akkaHttp = "10.1.5"

    val monocleVersion = "1.5.0"

  }

  /**
    * These dependencies are shared between JS and JVM projects
    * the special %%% function selects the correct version for each project
    */
//  val scalaTest = Seq("org.scalatest" %%% "scalatest" % "3.0.1" % "test")

  val circeVersion = "0.10.0"

  val sharedDependencies = Def.setting(
    Seq(
      "com.lihaoyi" %%% "scalatags" % "0.6.7",
      "com.github.julien-truffaut" %%% "monocle-core" % versions.monocleVersion,
      "com.github.julien-truffaut" %%% "monocle-macro" % versions.monocleVersion,
      "org.scalaz" %%% "scalaz-core" % versions.scalaZ,
      "com.github.julien-truffaut" %% "monocle-law" % versions.monocleVersion % "test",
//      "com.lihaoyi" %%% "pprint" % "0.5.3",
      "org.scalatest" %%% "scalatest" % "3.0.1" % "test"
    ) ++
      Seq(
        "io.circe" %%% "circe-core",
        "io.circe" %%% "circe-generic",
        "io.circe" %%% "circe-parser"
      ).map( _ % circeVersion )
  )

  /** Dependencies only used by the JVM project */
  val jvmDependencies = Def.setting(
    Seq(
      "de.heikoseeberger" %%% "akka-http-circe" % "1.22.0",
      "ch.megard" %%% "akka-http-cors" % "0.3.1",
      "com.github.dnvriend" %% "akka-persistence-inmemory" % versions.in_memory_journal,
      "com.typesafe.akka" %% "akka-persistence" % versions.akka,
      "org.iq80.leveldb" % "leveldb" % "0.10",
      "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
      "com.typesafe.akka" %% "akka-actor" % versions.akka,
      "com.typesafe.akka" %% "akka-slf4j" % versions.akka,
      "com.typesafe.akka" %% "akka-http" % versions.akkaHttp,
      "com.typesafe.akka" %% "akka-http-testkit" % versions.akkaHttp,
      "org.slf4j" % "slf4j-simple" % "1.7.25",
      "org.scalatest" %%% "scalatest" % "3.0.1" % "test"
    )
  )

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting(
    Seq(
      "com.lihaoyi" %%% "pprint" % "0.5.3",
      "biz.enef" %%% "slogging" % "0.5.3",
      "com.github.japgolly.scalajs-react" %%% "core" % versions.scalajsReact,
      "com.github.japgolly.scalajs-react" %%% "extra" % versions.scalajsReact,
      "com.github.japgolly.scalacss" %%% "ext-react" % versions.scalaCSS,
      "com.github.japgolly.scalacss" %%% "core" % versions.scalaCSS,
      "org.scala-js" %%% "scalajs-dom" % versions.scalaDom,
      "org.scalatest" %%% "scalatest" % "3.0.1" % "test"
    )
  )

}
