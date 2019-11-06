import java.io.{Console => _}

import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt._

object Settings {

//  val ex=exclude("com.chuusai","shapeless")
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
    val in_memory_journal = "2.5.15.2"

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
      "org.scalatest" %%% "scalatest"                  % versions.scalaTestVersion % "test",
      "org.typelevel" %%% "cats-core"                  % "2.0.0",
      "io.monix" %%% "monix"                           % "3.0.0",
      "org.typelevel"                                  %% "simulacrum" % "1.0.0"
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
      //      "me.shadaj"                               %% "scalapy-core" % "0.3.0",
//      "org.platanios"                           % "tensorflow-api_2.12" % "0.4.1"  ,
//      "org.vegas-viz"                           %% "vegas" % "0.3.11",
      "com.cibo"                                %% "scalastan" % "0.9.0",
      "org.scalanlp"                            %% "breeze" % "1.0",
      "org.scalanlp"                            %% "breeze-natives" % "1.0",
      "org.scalanlp"                            %% "breeze-viz" % "1.0",
      "org.apache.spark"                        %% "spark-sql" % "2.4.4",
      "com.stripe"                              %% "rainier-core" % "0.2.3",
//      "com.stripe"                              %% "rainier-repl" % "0.2.2",
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
      "org.scalatest" %%% "scalatest" % versions.scalaTestVersion % "test"
    )
  )

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting(
    Seq(
      "com.cibo" %%% "evilplot" % "0.6.3", // Use %%% instead of %% if you're using ScalaJS
      "com.lihaoyi" %%% "pprint"                      % "0.5.3",
      "com.github.japgolly.scalajs-react" %%% "core"  % versions.scalajsReact,
      "com.github.japgolly.scalajs-react" %%% "extra" % versions.scalajsReact,
      "com.github.japgolly.scalacss" %%% "ext-react"  % versions.scalaCSS,
      "com.github.japgolly.scalacss" %%% "core"       % versions.scalaCSS,
      "org.scala-js" %%% "scalajs-dom"                % versions.scalaDom,
      "io.github.nafg.css-dsl" %%% "bootstrap4"       % "0.4.0",
      "org.scalatest" %%% "scalatest"                 % versions.scalaTestVersion % "test" //,
      //      "com.github.outwatch.outwatch" %%% "outwatch"   % "f7c3081"
    )
  )

}
