package app.client.ui.components.rootComponents.cacheTestRootComp

//import app.client.ui.components.cache.hidden.ReRenderTriggerer
import app.client.ui.components.cache.exposed.CacheInterface
import app.shared.data.model.LineText
import app.shared.data.ref.{Ref, RefVal}
import org.scalajs.dom.html.Div
import slogging.LazyLogging

import scala.util.Random
//import app.shared.data.utils.PrettyPrint
import app.testHelpersShared.data.TestEntities
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.builder.Lifecycle
import japgolly.scalajs.react.vdom.html_<^._

import scala.concurrent.{ExecutionContextExecutor, Future}

class Backend($ : BackendScope[CacheTestRootCompProps, CacheTestRootCompState] ) {

  val incCounter: CallbackTo[Unit] = $.modState( s => s.copy( i = s.i + 1 ) )

  val triggerReRenderAndIncCounter: CallbackTo[Unit] =
    $.modState( s => s.copy( i = s.i + 1 ) )

  val incCounterFiveSecLater: CallbackTo[Unit] = Callback {
    import scala.scalajs.js.timers._
    setTimeout( 5000 ) { $.modState( s => s.copy( i = s.i + 1 ) ).runNow() }
  }

  val incCounterFiveSecLater_CalledFromComponentDidMount: CallbackTo[Unit] =
    Callback {
      import scala.scalajs.js.timers._
      println( "incCounterFiveSecLater_CalledFromComponentDidMount was called" )
      setTimeout( 5000 ) { $.modState( s => s.copy( i = s.i + 1 ) ).runNow() }
    }

  def render(state: CacheTestRootCompState, props: CacheTestRootCompProps ) = {

    val fetchDataFromServer: CallbackTo[Unit] =
      Callback {
        implicit def executionContext: ExecutionContextExecutor =
          scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
        import app.client.comm.REST.getEntity
        import io.circe.generic.auto._
        val ref: Ref[LineText] = Ref.makeWithUUID[LineText]( TestEntities.refValOfLineV0.r.uuid )

        val res: Future[Unit] = getEntity[LineText]( ref ).map(
          x => {
            println( s"az entity visszavage $x" )
            val lt: RefVal[LineText] = x
            $.modState( s => s.copy( lineTextOption = Some( lt ) ) ).runNow()
          }
        )

      }

    val lineSeparator: VdomTagOf[Div] = <.div( <.br, "------------", <.br )

    <.div(
      "State passed: ",
      state.toString,
      <.br,
      "Props passed:",
      props.toString,
      <.br,
      <.button( ^.onClick --> fetchDataFromServer, "Fetch data from server." ),
      <.br,
      lineSeparator,
      CacheAccessHelper.getLineTextFromCache,
      lineSeparator,
      CacheAccessHelper.getLineTextFromCache,
      lineSeparator,
      CacheAccessHelper.getLineTextFromCache
    )
  }

}

object CacheTestRootComp extends LazyLogging {

//  import java.time.Instant
//  def getTime={
//    val n: Instant = Instant.now()
//    n.toString
//  }
//  case class State(i: Int, lineTextOption: Option[RefVal[LineText]],timeStamp:String=getTime )

  class ReRenderTriggerer(private val causeReRender: Unit => Unit ) {

    def triggerReRender() = {
      val r = Random.nextInt( 1000 )
      logger.trace( s"before calling causeReRender() in ReRenderTriggerer.triggerReRender, random ID: $r" )
      causeReRender()
      logger.trace(
        s"after call has returned causeReRender() in ReRenderTriggerer.triggerReRender, random ID: $r"
      )
      ReRenderTriggerer.nrOfRerenders = ReRenderTriggerer.nrOfRerenders + 1
    }
  }

  object ReRenderTriggerer {
    var nrOfRerenders = 0
  }

  def toBeCalledByComponentDidMount(
      x: Lifecycle.Base[CacheTestRootCompProps, CacheTestRootCompState, Backend]
    ): CallbackTo[Unit] =
    Callback {
      println( "toBeCalledByComponentDidMount called - component did mount" )

      val reRenderTriggerer =
        new ReRenderTriggerer( _ => {
          logger.trace( "reRenderTriggerer was executed, this means a real re-render" )
          x.backend.incCounter.runNow() // WE TRIGGER HERE A REAL RE-RENDER
          logger.trace( "we have just increased the counter in the component" )
        } )

      CacheInterface.setReRenderTriggerer( reRenderTriggerer )

    }

  //noinspection TypeAnnotation
  //  case class RootComponentConstructorProvider()

  lazy val compConstructor =
    ScalaComponent
      .builder[CacheTestRootCompProps]( "Cache Experiment" )
      .initialState( CacheTestRootCompState( 42, None ) )
      .renderBackend[Backend]
      .componentDidMount( toBeCalledByComponentDidMount( _ ) )
      .componentDidUpdate(
        (x: Lifecycle.Base[CacheTestRootCompProps, CacheTestRootCompState, Backend]) =>
          Callback( {
            pprint.pprintln( "componented did Update" )
            pprint.pprintln( x, width = 10 )
//                      val px: String = pprint.apply(x).render
            val px: String = pprint.apply( x ).render
            println(
              s"component did update / eli render has completed\n x=$x \n pretty printed x : \n $px \n"
            )
          } )
      )
      .build

}
