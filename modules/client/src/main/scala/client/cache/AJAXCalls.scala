package client.cache

import client.Main
import client.cache.commands.{
  UpdateEntitiesInCacheCommand,
  UpdateEntityInCacheCmd
}
import client.ui.helpers.login.UserLoginStatusHandler
import shared.communication.{CanProvideRouteName}
import io.circe.{Decoder, Encoder}
//import io.circe.generic.JsonCodec
//import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom.ext.Ajax
import shapeless.Typeable
import shared.communication.persActorCommands.{Query, Response}
import shared.communication.persActorCommands.auth.QueryAuthWrapper
import shared.communication.persActorCommands.crudCMDs.{
  GetAllEntityiesForUserPersActCmd,
  UpdateEntitiesPersActorCmd,
  UpdateEntityPersActCmd
}
import shared.communication.persActorCommands.generalCmd.AdminQuery
import shared.dataStorage.model.{PWDNotHashed, Value}
import shared.dataStorage.relationalWrappers.RefToEntityOwningUser
import shared.dataStorage.stateHolder.UserMap

import scala.concurrent.ExecutionContextExecutor
import scala.util.Try

// router + header

object AJAXCalls {

  val ip   = Main.host
  val port = Main.port
//  val ip = "ec2-3-124-8-254.eu-central-1.compute.amazonaws.com"

  implicit def executionContext: ExecutionContextExecutor =
    scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

  def sendCommandToServerViaAJAXCall[
    Command: CanProvideRouteName: Encoder: Decoder
  ](in:            Command,
    runOnComplete: (Try[Command] => Unit)
  ): Unit = {

//    import io.circe.syntax._

    val headers: Map[String, String] = Map(
      "Content-Type" -> "application/json"
    )

    import io.circe.generic.auto._
    import io.circe.syntax._
    import io.circe.parser._

    val port = Main.port

    Ajax
      .post(
        s"http://$ip:$port/${implicitly[CanProvideRouteName[Command]].getRouteName}",
        in.asJson.spaces4,
        headers = headers
      )
      .map(_.responseText)
      .map(decode[Command](_).toOption.get)
      .onComplete(runOnComplete)

  }

  def sendCommandToServerWithAuthWrapper[
    Q <: Query: CanProvideRouteName: Encoder: Decoder
  ](in:            Q,
    pwd:           PWDNotHashed,
    runOnComplete: (Try[Response[Q]] => Unit)
  )(
    implicit
    encoder:     Encoder[QueryAuthWrapper[Q]],
    respDecoder: Decoder[Response[Q]]
  ): Unit = {

    //    import io.circe.syntax._

    val headers: Map[String, String] = Map(
      "Content-Type" -> "application/json"
    )
    val query = QueryAuthWrapper(in, pwd)

//    import io.circe.generic.auto._
//    import io.circe.syntax._
//    import io.circe.generic.auto._
//    import io.circe.syntax._
    import io.circe.parser._

    val jsonStringToSend: String = query.asJson.spaces4

    Ajax
      .post(
        s"http://$ip:$port/${implicitly[CanProvideRouteName[Q]].getRouteName}",
        jsonStringToSend,
        headers = headers
      )
      .map(_.responseText)
      .map(decode[Response[Q]](_).toOption.get)
      .onComplete(runOnComplete)

  }

//  def sendCommandToServerViaAJAXCallAndParseResponse[
//    Command: JSONConvertable: CanProvideRouteName: Encoder: Decoder
//  ](in:            Command,
//    runOnComplete: (Try[Response[Command]] => Unit)
//  )(
//    implicit
//    respDecoder: JSONConvertable[Response[Command]]
//  ): Unit = {
//
//    //    import io.circe.syntax._
//
//    val headers: Map[String, String] = Map(
//      "Content-Type" -> "application/json"
//    )
//
//    import io.circe.generic.auto._
//    import io.circe.syntax._
//
//    Ajax
//      .post(
//        s"http://$ip:$port/${implicitly[CanProvideRouteName[Command]].getRouteName}",
//        in.asJson.spaces4,
//        headers = headers
//      )
//      .map(_.responseText)
//      .map(
//        implicitly[JSONConvertable[Response[Command]]]
//          .fromJSONToObject(_)
//      )
//      .onComplete(runOnComplete)
//
//  }

  def populateEntityCache[V <: Value[V]](
    c:               Cache[V],
    pwdNotHashedPar: PWDNotHashed
  ) : Unit = {

    import io.circe.syntax._

    implicit val enc =
      QueryAuthWrapper.encoder[GetAllEntityiesForUserPersActCmd]()

    lazy val owner: RefToEntityOwningUser =
      RefToEntityOwningUser.makeFromRef(
        UserLoginStatusHandler.getUserLoginStatusDev.userOption.get.ref
      )

    def ajaxReturnHandler
      : Try[Response[GetAllEntityiesForUserPersActCmd]] => Unit = {
      (x: Try[Response[GetAllEntityiesForUserPersActCmd]]) =>
        {
          val res1: UserMap = x.toOption.get.cmd.res.get

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

    val cmdToSend =
      GetAllEntityiesForUserPersActCmd(
        owner,
        None
      )

    sendCommandToServerWithAuthWrapper(cmdToSend,
                                       pwdNotHashedPar,
                                       ajaxReturnHandler)

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

//  def saveDataOnServer(pwd: String): Unit = {
//    sendCommandToServerViaAJAXCallAndParseResponse[
//      AdminQuery
//    ](
//      AdminQuery(
//        AdminQuery.CommandStrings.saveData
//      ), { resp: Try[Response[AdminQuery]] =>
//        println(resp)
//      }
//    )
//  }

}
