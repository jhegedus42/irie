import java.io.{Console => _}

import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Settings {

  /** The name of your application */
  /** The version of your application */
  val version = "1.1.4"

//  val targetDir = (classDirectory in Compile).value / "web"

  /** Options for the scala compiler */
  val scalacOptions = Seq(
//    "-deprecation", // Emit warning and location for usages of deprecated APIs.
//    "-encoding",
//    "utf-8", // Specify character encoding used by source files.
//    "-explaintypes", // Explain type errors in more detail.
//    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
    "-language:experimental.macros", // Allow macro definition (besides implementation and application)
    "-language:higherKinds", // Allow higher-kinded types
    "-language:implicitConversions", // Allow definition of implicit functions called views
    "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
    "-Xfuture", // Turn on future language features.
    "-Yno-adapted-args", // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
    "-Ypartial-unification", // Enable partial unification in type constructor inference
    //    "-Xlint",
//    "-unchecked",
    // "-deprecation",
    //   "-feature",
    "-Yrangepos",
//    "-Ypartial-unification",
    "-nowarn" //,
    // https://docs.scala-lang.org/overviews/compiler-options/index.html
    //,
//  "-Xfatal-warnings"  // New lines for each options

    //
//    "-Ylog-classpath"
//    "-Xlog-implicits"
  )

  /** Declare global dependency versions here to avoid mismatches in multi part dependencies */
  object versions {
    val scala        = "2.12.8"
    val scalaDom     = "0.9.6"
    val scalajsReact = "1.3.1"
    val scalaCSS     = "0.5.5"
    val scalaZ       = "7.2.26"

    val akka              = "2.5.23"
    val akkaHttp = "10.1.9"

    val monocleVersion   = "1.5.0"
    val scalaTestVersion = "3.0.8"

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
      "org.scalatest" %%% "scalatest"                  % versions.scalaTestVersion % "test"
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
      // test:
      "org.scalatest" %%% "scalatest" % versions.scalaTestVersion % "test"
    )
  )

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting(
    Seq(
      "com.github.japgolly.scalajs-react" %%% "core"  % versions.scalajsReact,
      "com.github.japgolly.scalajs-react" %%% "extra" % versions.scalajsReact,
      "com.github.japgolly.scalacss" %%% "ext-react"  % versions.scalaCSS,
      "com.github.japgolly.scalacss" %%% "core"       % versions.scalaCSS,
      "org.scala-js" %%% "scalajs-dom"                % versions.scalaDom,
      "io.github.nafg.css-dsl" %%% "bootstrap4"       % "0.4.0",
      "org.scalatest" %%% "scalatest"                 % versions.scalaTestVersion % "test" ,
       "me.shadaj" %%% "slinky-core" % "0.6.3",
       "me.shadaj" %%% "slinky-web" % "0.6.3"


    )
  )

}
