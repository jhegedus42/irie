package app.client.ui.routing.cache.exposed

import app.shared.data.model.LineText
import app.shared.data.ref.TypedRef
import CacheStates.CacheState
import app.client.ui.routing.cache.hidden.CommunicationHandlerForEntityCache

object ReRenderTriggererHolderSingletonGloballyAccessibleObject {
  private var triggerer: Option[ReRenderTriggerer] = None

  def setTriggerer(reRenderTriggerer: ReRenderTriggerer ) = {

    println(
      s"METHOD CALL : `def setTriggerer(reRenderTriggerer: ReRenderTriggerer )`, where :" +
        s"reRenderTriggerer=$reRenderTriggerer"
    )

    println( s"the old value of triggerer is : $triggerer" )

    println( "we have just called set triggerer" )

    println( s"setTrigger was called, reRenderTriggerer= $reRenderTriggerer" )

    triggerer = Some( reRenderTriggerer );

    println( s"the new value of triggerer is : $triggerer" )

    println( s"METHOD CALL ENDED --------------------" )
  }

  def triggerReRender() = {

    println(
      "METHOD CALL --- " +
        "`object ReRenderTriggererHolderSingletonGloballyAccessibleObject.triggerReRender()`"
    )

    val t = triggerer

    val ne = (t.nonEmpty)

    println(
      s"(t.nonEmpty) = $ne, in other words, " +
        s"is the triggerer already set from None to Some ?" +
        s"This is interesting because we are in the `triggerReRender`" +
        s"function and want to use it to cause a re-render."
    )

    if (ne) {

      val tr_naked: ReRenderTriggerer = t.head

      println( s"the value of the triggerer, 'the naked value'= $tr_naked" )

      tr_naked.triggerReRender()

      println( s"now we have called triggerReRender(), did anything happen ?" )
      println( s"METHOD CALL ENDED ---------------- " )

    }
  }

  case class ReRenderTriggerer(triggerReRender: () => Unit )

}

class CacheInterface() {

  import io.circe.generic.auto._
  import io.circe.{Decoder, Encoder}

  private lazy val cacheLineText: CommunicationHandlerForEntityCache[LineText] =
    new CommunicationHandlerForEntityCache[LineText]( this )

  def readLineText(ref: TypedRef[LineText] ): CacheState[LineText] = {

    val res: CacheState[LineText] = cacheLineText.readEntity( ref )
    res
  }

  private[cache] def reRenderShouldBeTriggered() = {
    println(
      s"METHOD CALL --- CacheInterface.reRenderShouldBeTriggered() --- " +
        "so now we try to trigger a re-render in reRenderShouldBeTriggered()"
    )
    ReRenderTriggererHolderSingletonGloballyAccessibleObject.triggerReRender()

    println(
      s"METHOD CALL ENDED for   `CacheInterface.reRenderShouldBeTriggered()` ---------------- "
    )
  }

}
