package client.cache

import client.Main
import client.cache.commands.{UpdateEntitiesInCacheCommand, UpdateEntityInCacheCmd}
import client.ui.helpers.login.UserLoginStatusHandler
import shared.crudRESTCallCommands.{CanProvideRouteName, JSONConvertable}
import io.circe.{Decoder, Encoder}
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom.ext.Ajax
import shapeless.Typeable
import shared.crudRESTCallCommands.persActorCommands.Response
import shared.crudRESTCallCommands.persActorCommands.crudCMDs.{GetAllEntityiesForUserPersActCmd, UpdateEntitiesPersActorCmd, UpdateEntityPersActCmd}
import shared.crudRESTCallCommands.persActorCommands.generalCmd.GeneralPersActorCmd
import shared.dataStorage.model.Value
import shared.dataStorage.relationalWrappers.RefToEntityOwningUser
import shared.dataStorage.stateHolder.UserMap

import scala.concurrent.ExecutionContextExecutor
import scala.util.Try

object AJAXCalls {

  val ip = Main.host
//  val ip = "ec2-3-124-8-254.eu-central-1.compute.amazonaws.com"

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

    val port=Main.port

    Ajax
      .post(
        s"http://$ip:$port/${implicitly[CanProvideRouteName[Command]].getRouteName}",
        in.asJson.spaces4,
        headers = headers
      )
      .map(_.responseText)
      .map(
        implicitly[JSONConvertable[Command]].fromJSONToObject(_)
      )
      .onComplete(runOnComplete)

  }

  def sendCommandToServerViaAJAXCallAndParseResponse[
    Command: JSONConvertable: CanProvideRouteName: Encoder: Decoder
  ](in:            Command,
    runOnComplete: (Try[Response[Command]] => Unit)
  )(
    implicit
    respDecoder: JSONConvertable[Response[Command]]
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
        implicitly[JSONConvertable[Response[Command]]]
          .fromJSONToObject(_)
      )
      .onComplete(runOnComplete)

  }

  def populateEntityCache[V <: Value[V]](c: Cache[V]): Unit = {

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

          c.cellLoop
            .listen(
              (cacheMap: CacheMap[V]) => {
                val cachesContent = cacheMap.getPrettyPrintedString
                println(
                  s"--------------------------------------\n" +
                    s"updated cacheMap (with type: ${cacheMap.getTypeName})\n" +
                    s"${cachesContent}\n" +
                    s"${cacheMap.toString}\n" +
                    s"------------------------------------\n"
                )
              }
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

    // test
    val handleReturn = { (t: Try[UpdateEntityPersActCmd]) =>
    }
    // todo later, handle OCC error
    sendCommandToServerViaAJAXCall(updateEntityPersActCmd,
                                   handleReturn)
  }

  def updateEntitiesOnServer[V <: Value[V]: Encoder: Typeable](
    updateEntityInCacheCmd: UpdateEntitiesInCacheCommand[V]
  ): Unit = {

    val updateEntitiesPersActCmd: UpdateEntitiesPersActorCmd =
      UpdateEntitiesInCacheCommand.toUpdateEntityPersActCmd(
        updateEntityInCacheCmd
      )

    // test
    val handleReturn = { (t: Try[UpdateEntitiesPersActorCmd]) =>
    }
    // todo later, handle OCC error
    sendCommandToServerViaAJAXCall(updateEntitiesPersActCmd,
                                   handleReturn)
  }

  def saveDataOnServer(): Unit = {
    sendCommandToServerViaAJAXCallAndParseResponse[
      GeneralPersActorCmd
    ](
      GeneralPersActorCmd(GeneralPersActorCmd.CommandStrings.saveData),{
        resp: Try[Response[GeneralPersActorCmd]] =>
          println(resp)
      }
    )
  }

}
