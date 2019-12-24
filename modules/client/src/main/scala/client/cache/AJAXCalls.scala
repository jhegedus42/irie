package client.cache

import client.ui.helpers.login.UserLoginStatusHandler
import shared.crudRESTCallCommands.persActorCommands.{
  GetAllEntityiesForUserPersActCmd,
  UpdateEntityPersActCmd
}
import shared.crudRESTCallCommands.{
  CanProvideRouteName,
  JSONConvertable
}
import io.circe.Encoder
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom.ext.Ajax
import shapeless.Typeable
import shared.dataStorage.{RefToEntityOwningUser, User, Value}
import shared.dataStorage.stateHolder.UserMap

import scala.concurrent.ExecutionContextExecutor
import scala.util.Try

object AJAXCalls {
  val ip = "localhost"

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def sendCommandToServerViaAJAXCall[
    Command: JSONConvertable: CanProvideRouteName: Encoder
  ](in:            Command,
    runOnComplete: (Try[Command] => Unit)
  ): Unit = {

//    import io.circe.syntax._

    val headers: Map[String, String] = Map(
      "Content-Type" -> "application/json"
    )

    import io.circe.generic.auto._
    import io.circe.syntax._

    Ajax
      .post(
        s"http://$ip:8080/${implicitly[CanProvideRouteName[Command]].getRouteName}",
        in.asJson.spaces4,
        headers = headers
      )
      .map(_.responseText)
      .map(
        implicitly[JSONConvertable[Command]].fromJSONToObject(_)
      )
      .onComplete(runOnComplete)

  }

  def populateUserEntityCache(): Unit = {

    import io.circe.syntax._

    lazy val owner: RefToEntityOwningUser =
      RefToEntityOwningUser.makeFromRef(
        UserLoginStatusHandler.getUserLoginStatusDev.userOption.get.ref
      )

    def ajaxReturnHandler
      : Try[GetAllEntityiesForUserPersActCmd] => Unit = {
      (x: Try[GetAllEntityiesForUserPersActCmd]) =>
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

    sendCommandToServerViaAJAXCall(
      GetAllEntityiesForUserPersActCmd(owner, None),
      ajaxReturnHandler
    )

  }

  def updateEntityOnServer[V <: Value[V]: Encoder: Typeable](
    updateEntityInCacheCmd: UpdateEntityInCacheCmd[V]
  ): Unit = {

    val updateEntityPersActCmd: UpdateEntityPersActCmd =
      UpdateEntityInCacheCmd.toUpdateEntityPersActCmd(
        updateEntityInCacheCmd
      )

    val handleReturn = { (t: Try[UpdateEntityPersActCmd]) =>
    }
    sendCommandToServerViaAJAXCall(updateEntityPersActCmd,
                                   handleReturn)
  }

}
