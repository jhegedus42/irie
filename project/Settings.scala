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
    "-Yrangepos",
    "-nowarn"
    // https://docs.scala-lang.org/overviews/compiler-options/index.html
    //,
//  "-Xfatal-warnings"  // New lines for each options

    //
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

    val akka              = "2.5.23"
    val in_memory_journal = "2.5.15.2"

    val akkaHttp = "10.1.9"

    val monocleVersion = "1.5.0"

  }

  /**
    * These dependencies are shared between JS and JVM projects
    * the special %%% function selects the correct version for each project
    */
  val scalaTest = Seq()

  val circeVersion = "0.11.1"

  val sharedDependencies = Def.setting(
    Seq(
      "com.lihaoyi" %%% "scalatags"                    % "0.6.7",
      "com.github.julien-truffaut" %%% "monocle-core"  % versions.monocleVersion,
      "com.github.julien-truffaut" %%% "monocle-macro" % versions.monocleVersion,
      "org.scalaz" %%% "scalaz-core"                   % versions.scalaZ,
      "org.scalatest" %%% "scalatest"                  % "3.0.8" % "test"
    ) ++
      Seq(
        "io.circe" %%% "circe-core",
        "io.circe" %%% "circe-generic",
        "io.circe" %%% "circe-parser"
      ).map(_ % circeVersion)
  )

  /** Dependencies only used by the JVM project */
  val jvmDependencies = Def.setting(
    Seq(
      "org.slf4j"                               % "slf4j-simple" % "1.7.25",
      "com.typesafe.akka"                       %% "akka-slf4j" % versions.akka,
      "com.typesafe.akka"                       %% "akka-actor" % versions.akka,
      "com.typesafe.akka"                       %% "akka-stream-testkit" % versions.akka,
      "com.typesafe.akka"                       %% "akka-http" % versions.akkaHttp,
      "com.typesafe.akka"                       %% "akka-http-testkit" % versions.akkaHttp,
      "de.heikoseeberger" %%% "akka-http-circe" % "1.27.0",
      // akka-persistence :
      "com.typesafe.akka"   %% "akka-persistence"          % versions.akka,
      "com.github.dnvriend" %% "akka-persistence-inmemory" % versions.in_memory_journal,
      // leveldb :
      "org.iq80.leveldb"          % "leveldb"        % "0.10",
      "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
      // test:
      "org.scalatest" %%% "scalatest" % "3.0.8" % "test"
    )
  )

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting(
    Seq(
      "com.lihaoyi" %%% "pprint"                      % "0.5.3",
      "com.github.japgolly.scalajs-react" %%% "core"  % versions.scalajsReact,
      "com.github.japgolly.scalajs-react" %%% "extra" % versions.scalajsReact,
      "com.github.japgolly.scalacss" %%% "ext-react"  % versions.scalaCSS,
      "com.github.japgolly.scalacss" %%% "core"       % versions.scalaCSS,
      "org.scala-js" %%% "scalajs-dom"                % versions.scalaDom,
      "io.github.nafg.css-dsl" %%% "bootstrap4"       % "0.4.0",
      "org.scalatest" %%% "scalatest"                 % "3.0.8" % "test"
    )
  )

}
