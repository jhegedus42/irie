//import org.scalajs.jsenv.ExternalJSEnv.{AsyncExtRunner, ExtRunner}
import org.scalajs.jsenv.nodejs.AbstractNodeJSEnv
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

/**
  * Application settings. Configure the build for your application here.
  * You normally don't have to touch the actual build definition after this.
  */
/*                     __                                               *\
**     ________ ___   / /  ___      __ ____  Scala.js sbt plugin        **
**    / __/ __// _ | / /  / _ | __ / // __/  (c) 2013, LAMP/EPFL        **
**  __\ \/ /__/ __ |/ /__/ __ |/_// /_\ \    http://scala-js.org/       **
** /____/\___/_/ |_/____/_/ | |__/ /____/                               **
**                          |/____/                                     **
\*                                                                      */

// package org.scalajs.jsenv.nodejs

import java.io.{Console => _, _}

import org.scalajs.core.ir.Utils.escapeJS
import org.scalajs.core.tools.io._
import org.scalajs.core.tools.jsdep.ResolvedJSDependency
import org.scalajs.jsenv._

class JSDOMNodeJSEnv2(
    @deprecatedName('nodejsPath ) executable: String              = "node",
    @deprecatedName('addArgs ) args:          Seq[String]         = Seq.empty,
    @deprecatedName('addEnv ) env:            Map[String, String] = Map.empty)
    extends AbstractNodeJSEnv( executable, args, env, sourceMap = false ) {

  protected def vmName: String = "Node.js with JSDOM"

  override def jsRunner(libs: Seq[ResolvedJSDependency], code: VirtualJSFile ): JSRunner = {
    new DOMNodeRunner( libs, code )
  }

  override def asyncRunner(libs: Seq[ResolvedJSDependency], code: VirtualJSFile ): AsyncJSRunner = {
    new AsyncDOMNodeRunner( libs, code )
  }

  override def comRunner(libs: Seq[ResolvedJSDependency], code: VirtualJSFile ): ComJSRunner = {
    new ComDOMNodeRunner( libs, code )
  }

  protected class DOMNodeRunner(libs: Seq[ResolvedJSDependency], code: VirtualJSFile )
      extends ExtRunner( libs, code ) with AbstractDOMNodeRunner

  protected class AsyncDOMNodeRunner(libs: Seq[ResolvedJSDependency], code: VirtualJSFile )
      extends AsyncExtRunner( libs, code ) with AbstractDOMNodeRunner

  protected class ComDOMNodeRunner(libs: Seq[ResolvedJSDependency], code: VirtualJSFile )
      extends AsyncDOMNodeRunner( libs, code ) with NodeComJSRunner

  protected trait AbstractDOMNodeRunner extends AbstractNodeRunner {

    protected def codeWithJSDOMContext(): Seq[VirtualJSFile] = {
      val scriptsJSPaths = getLibJSFiles().map {
        case file: FileVirtualFile => file.path
        case file => libCache.materialize( file ).getAbsolutePath
      }
      val scriptsStringPath = scriptsJSPaths.map( '"' + escapeJS( _ ) + '"' )
      val jsDOMCode = {
        s"""
           |(function () {
           |  var jsdom;
           |  try {
           |    jsdom = require("jsdom/lib/old-api.js"); // jsdom >= 10.x
           |  } catch (e) {
           |    jsdom = require("jsdom"); // jsdom <= 9.x
           |  }
           |
           |  var windowKeys = [];
           |
           |  jsdom.env({
           |    html: "",
           |    virtualConsole: jsdom.createVirtualConsole().sendTo(console),
           |    created: function (error, window) {
           |      if (error == null) {
           |        window["__ScalaJSEnv"] = __ScalaJSEnv;
           |        window["scalajsCom"] = global.scalajsCom;
           |        windowKeys = Object.keys(window);
           |      } else {
           |        console.log(error);
           |      }
           |    },
           |    scripts: [${scriptsStringPath.mkString( ", " )}],
           |    onload: function (window) {
           |      jsdom.changeURL(window, "http://localhost:8043");
           |      for (var k in window) {
           |        if (windowKeys.indexOf(k) == -1)
           |          global[k] = window[k];
           |      }
           |
           |      ${code.content}
           |    }
           |  });
           |})();
           |""".stripMargin
      }
      Seq( new MemVirtualJSFile( "codeWithJSDOMContext.js" ).withContent( jsDOMCode ) )
    }

    override protected def getJSFiles(): Seq[VirtualJSFile] =
      initFiles() ++ customInitFiles() ++ codeWithJSDOMContext()

    /** Libraries are loaded via scripts in Node.js */
    override protected def getLibJSFiles(): Seq[VirtualJSFile] =
      libs.map( _.lib )

    // Send code to Stdin
    override protected def sendVMStdin(out: OutputStream ): Unit = {
      /* Do not factor this method out into AbstractNodeRunner or when mixin in
       * the traits it would use AbstractExtRunner.sendVMStdin due to
       * linearization order.
       */
      sendJS( getJSFiles(), out )
    }
  }

}

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
    val scala        = "2.12.4"
    val scalaDom     = "0.9.6"
    val scalajsReact = "1.1.1"
    val scalaCSS     = "0.5.5"

    val akka              = "2.5.1"
    val in_memory_journal = "2.5.1.1"

    val akkaHttp = "10.1.5"

    val monocleVersion = "1.4.0"

  }

  /**
    * These dependencies are shared between JS and JVM projects
    * the special %%% function selects the correct version for each project
    */
//  val scalaTest = Seq("org.scalatest" %%% "scalatest" % "3.0.1" % "test")

  val circeVersion = "0.10.0"

  val sharedDependencies = Def.setting(
    Seq(
//      "org.scalactic" %%% "scalactic_2.12" % "3.0.4"
//      "com.github.johnreedlol" %% "scala-trace-debug" % "4.5.0",
      "com.lihaoyi" %%% "scalatags" % "0.6.7",
      "com.github.julien-truffaut" %%% "monocle-core" % versions.monocleVersion,
      "com.github.julien-truffaut" %%% "monocle-macro" % versions.monocleVersion,
      "org.scalaz" %%% "scalaz-core" % "7.2.16",
      "com.github.julien-truffaut" %% "monocle-law" % versions.monocleVersion % "test",
//      "com.beachape" %%% "enumeratum-circe" % "1.5.14", //,
      "com.lihaoyi" %%% "pprint" % "0.5.3",
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
      "de.heikoseeberger" %% "akka-http-circe" % "1.22.0",
//        "com.beachape" %% "enumeratum-circe" % "1.5.14",
      "ch.megard" %% "akka-http-cors" % "0.3.1",
      "com.github.dnvriend" %% "akka-persistence-inmemory" % versions.in_memory_journal,
      "com.typesafe.akka" %% "akka-persistence" % versions.akka,
      "org.iq80.leveldb" % "leveldb" % "0.10",
      "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
      "com.typesafe.akka" %% "akka-actor" % versions.akka,
      "com.typesafe.akka" %% "akka-slf4j" % versions.akka,
//          "ch.qos.logback"    % "logback-classic"            % "1.1.2",
      "com.typesafe.akka" %% "akka-http" % versions.akkaHttp,
      "com.typesafe.akka" %% "akka-http-testkit" % versions.akkaHttp,
      "org.slf4j" % "slf4j-simple" % "1.7.25",
//      "com.typesafe.akka" %% "akka-http-spray-json" % versions.akkaHttp,
      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    )
  )

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting(
    Seq(
      "com.lihaoyi" %%% "pprint" % "0.5.3",
      "biz.enef" %%% "slogging" % "0.5.3",
//      "com.beachape" %%% "enumeratum-circe" % "1.5.14",
      "com.github.japgolly.scalajs-react" %%% "core" % versions.scalajsReact,
      "com.github.japgolly.scalajs-react" %%% "extra" % versions.scalajsReact,
      "com.github.japgolly.scalacss" %%% "ext-react" % versions.scalaCSS,
      "com.github.japgolly.scalacss" %%% "core" % versions.scalaCSS,
      "org.scala-js" %%% "scalajs-dom" % versions.scalaDom,
      "org.scalatest" %%% "scalatest" % "3.0.1" % "test"
    )
  )

}
