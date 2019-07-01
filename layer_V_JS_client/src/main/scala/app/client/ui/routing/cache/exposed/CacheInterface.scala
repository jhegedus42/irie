package app.client.ui.routing.cache.exposed

import app.shared.data.model.LineText
import app.shared.data.ref.TypedRef
import CacheStates.CacheState
import app.client.ui.routing.cache.hidden.CommunicationHandlerForEntityCache
import slogging.LazyLogging


object ReRenderTriggererHolderSingletonGloballyAccessibleObject extends LazyLogging {
  private var triggerer: Option[ReRenderTriggerer] = None

  def setTriggerer(reRenderTriggerer: ReRenderTriggerer) = {

    println(s"the old value of triggerer is : $triggerer")

    println("we have just called set triggerer")

    logger.trace("setTrigger was called",reRenderTriggerer)

    triggerer = Some(reRenderTriggerer);

    println(s"the new value of triggerer is : $triggerer")
  }

  def triggerReRender() = {

    println("we are now in triggerReRender")

    val t = triggerer

    val ne = (t.nonEmpty)


    println(s"(t.nonEmpty) = $ne, in other words, " +
      s"is the triggerer already set from None to Some ?" +
      s"This is interesting because we are in the `triggerReRender`" +
      s"function and want to use it to cause a re-render.")



    if (ne) {

      val tr_naked: ReRenderTriggerer = t.head

      println(s"the value of the triggerer, 'the naked value'= $tr_naked")

      tr_naked.triggerReRender()

      println(s"now we have called triggerReRender(), did anything happen ?")

    }
  }

  case class ReRenderTriggerer(triggerReRender: () => Unit)

}

class CacheInterface() extends LazyLogging {

  import io.circe.generic.auto._
  import io.circe.{Decoder, Encoder}

  private lazy val cacheLineText: CommunicationHandlerForEntityCache[LineText] =
    new CommunicationHandlerForEntityCache[LineText](this)

  def readLineText(ref: TypedRef[LineText]): CacheState[LineText] = {

    val res: CacheState[LineText] = cacheLineText.readEntity(ref)
    res
  }


  private[cache] def reRenderShouldBeTriggered() = {
    println("so now we try to trigger a re-render in reRenderShouldBeTriggered()")
      ReRenderTriggererHolderSingletonGloballyAccessibleObject.triggerReRender()

  }

}


