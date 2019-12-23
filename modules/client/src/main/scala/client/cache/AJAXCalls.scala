package client.cache

import client.ui.helpers.login.UserLoginStatusHandler
import shared.crudRequests.persActorCommands.GetAllEntityiesForUser
import shared.crudRequests.{CanProvideRouteName, JSONConvertable}
import io.circe.Encoder
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom.ext.Ajax
import shared.dataStorage.{RefToEntityOwningUser, User}
import shared.dataStorage.stateHolder.UserMap

import scala.concurrent.ExecutionContextExecutor
import scala.util.Try

object AJAXCalls {
  val ip = "localhost"

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def ajaxCall[V: JSONConvertable: CanProvideRouteName: Encoder](
    in:            V,
    runOnComplete: (Try[V] => Unit)
  ): Unit = {

    import io.circe.syntax._

    val headers: Map[String, String] = Map(
      "Content-Type" -> "application/json"
    )

    import io.circe.generic.auto._
    import io.circe.syntax._

    Ajax
      .post(
        s"http://$ip:8080/${implicitly[CanProvideRouteName[V]].getRouteName}",
        in.asJson.spaces4,
        headers = headers
      )
      .map(_.responseText)
      .map(
        implicitly[JSONConvertable[V]].fromJSONToObject(_)
      )
      .onComplete(runOnComplete)

  }

  def populateUserEntityCache(): Unit = {

    import io.circe.syntax._

    lazy val owner: RefToEntityOwningUser =
      RefToEntityOwningUser.makeFromRef(
        UserLoginStatusHandler.getUserLoginStatusDev.userOption.get.ref
      )

    def ajaxReturnHandler: Try[GetAllEntityiesForUser] => Unit = {
      (x: Try[GetAllEntityiesForUser]) =>
        {
          val res1: UserMap = x.toOption.get.res.get

          Cache.streamToSetInitialCacheState.send(res1)
          println(res1)

          Cache.user.cellLoop
            .listen(
              (cacheMap: CacheMap[User]) =>
                println(s"updated cacheMap:${cacheMap}")
            )
        }
    }

    // todonow continue here ...
    //  something is not working here
    //  cache is not populated on startup

    ajaxCall(GetAllEntityiesForUser(owner, None), ajaxReturnHandler)

  }

}
