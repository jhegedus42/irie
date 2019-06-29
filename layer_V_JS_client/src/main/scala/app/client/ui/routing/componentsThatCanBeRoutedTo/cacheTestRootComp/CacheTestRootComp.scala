package app.client.ui.routing.componentsThatCanBeRoutedTo.cacheTestRootComp

//import app.client.ui.components.cache.hidden.ReRenderTriggerer
import app.shared.data.model.LineText
import app.shared.data.ref.{TypedRef, RefVal}
import org.scalajs.dom.html.Div
import slogging.LazyLogging

import app.testHelpersShared.data.TestEntities
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scala.concurrent.{ExecutionContextExecutor, Future}

class Backend($: BackendScope[CacheTestRootCompProps, CacheTestRootCompState]) {

  val incCounter: CallbackTo[Unit] = $.modState(s => s.copy(i = s.i + 1))


  def render(state: CacheTestRootCompState, props: CacheTestRootCompProps) = {

    val cache = props.cacheInterface
    val fetchDataFromServer: CallbackTo[Unit] =
      Callback {
        implicit def executionContext: ExecutionContextExecutor =
          scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
        import app.client.REST.getEntity
        import io.circe.generic.auto._
        val ref: TypedRef[LineText] = TypedRef.makeWithUUID[LineText](TestEntities.refValOfLineV0.r.uuid)

        val res: Future[Unit] = getEntity[LineText](ref).map(
          x => {
            println(s"az entity visszavage $x")
            val lt: RefVal[LineText] = x
            $.modState(s => s.copy(lineTextOption = Some(lt))).runNow()
          }
        )

      }

    val lineSeparator: VdomTagOf[Div] = <.div(<.br, "------------", <.br)

    <.div(
      "State passed: ",
      state.toString,
      <.br,
      "Props passed:",
      props.toString,
      <.br,
      <.button(^.onClick --> fetchDataFromServer, "Fetch data from server."),
      <.br,
      lineSeparator,
      CacheAccessHelper.getLineTextFromCache(cache),
      lineSeparator,
      CacheAccessHelper.getLineTextFromCache(cache),
      lineSeparator,
      CacheAccessHelper.getLineTextFromCache(cache),
    )
  }

}


object CacheTestRootComp extends LazyLogging {


  lazy val compConstructor =
    ScalaComponent
      .builder[CacheTestRootCompProps]("Cache Experiment")
      .initialState(CacheTestRootCompState(42, None))
      .renderBackend[Backend]
      .build

}


object _Archive {

  //  THIS RE-RENDER TRIGGERER IS A HACK, IT SHOULD NOT BE HERE
  //  it is called from the `componentDidMount` ... so that this component
  //  can be re-render from "something" when some ajax comes back...

  //  for example, but that should not happen here... all the time...
  //  but rather in some parent component


  //

  // .componentDidMount(ReRenderTriggererContainer.toBeCalledByComponentDidMount(_))
  // WE ALSO DO NOT NEED THIS ^^^ at least NOT HERE !
  //


  //object ReRenderTriggererContainer {
  //
  //  def toBeCalledByComponentDidMount(
  //                                     x: Lifecycle.Base[CacheTestRootCompProps, CacheTestRootCompState, Backend]
  //                                   ): CallbackTo[Unit] =
  //    Callback {
  //      println("toBeCalledByComponentDidMount called - component did mount")
  //
  //      val reRenderTriggerer =
  //        new ReRenderTriggerer(_ => {
  //          //          logger.trace( "reRenderTriggerer was executed, this means a real re-render" )
  //          x.backend.incCounter.runNow() // WE TRIGGER HERE A REAL RE-RENDER
  //          //          logger.trace( "we have just increased the counter in the component" )
  //        })
  //
  //      CacheInterface.setReRenderTriggerer(reRenderTriggerer)
  //
  //    }
  //
  //  class ReRenderTriggerer(private val causeReRender: Unit => Unit) {
  //
  //    def triggerReRender() = {
  //      val r = Random.nextInt(1000)
  //      //      logger.trace( s"before calling causeReRender() in ReRenderTriggerer.triggerReRender, random ID: $r" )
  //      causeReRender()
  //      //      logger.trace(
  //      //        s"after call has returned causeReRender() in ReRenderTriggerer.triggerReRender, random ID: $r"
  //      //      )
  //      ReRenderTriggerer.nrOfRerenders = ReRenderTriggerer.nrOfRerenders + 1
  //    }
  //  }
  //
  //  object ReRenderTriggerer {
  //    var nrOfRerenders = 0
  //  }
  //
  //}


  // NOTE - THIS BELOW CAN BE PUT INTO THE `compConstructor` above - IF it is needed for SOMETHING ???
  //      .componentDidUpdate(
  //        (x: Lifecycle.Base[CacheTestRootCompProps, CacheTestRootCompState, Backend]) =>
  //          Callback({
  //            pprint.pprintln("componented did Update")
  //            pprint.pprintln(x, width = 10)
  //            //                      val px: String = pprint.apply(x).render
  //            val px: String = pprint.apply(x).render
  //            println(
  //              s"component did update / eli render has completed\n x=$x \n pretty printed x : \n $px \n"
  //            )
  //          })
  //      )

  //  import java.time.Instant
  //  def getTime={
  //    val n: Instant = Instant.now()
  //    n.toString
  //  }
  //  case class State(i: Int, lineTextOption: Option[RefVal[LineText]],timeStamp:String=getTime )


}