package client.cache.comm

import client.cache.{Cache, CacheMap}
import client.ui.login.UserLoginStatusHandler
import comm.crudRequests.JSONConvertable
import comm.crudRequests.persActorCommands.GetAllEntityiesForUser
import dataStorage.stateHolder.UserMap
import dataStorage.{RefToEntityOwningUser, User}
import io.circe.Json
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom.ext.Ajax

import scala.concurrent.ExecutionContextExecutor
import scala.util.Try

object TestAjaxRequest {

  def populateUserEntityCache()
//                             (
//    implicit
//    a: Map[Ref[User], TypedReferencedValue[User]]
//  )
    : Unit = {

    implicit def executionContext
      : ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    import io.circe.syntax._

    val headers: Map[String, String] = Map(
      "Content-Type" -> "application/json"
    )

//    val ip = "commserver.asuscomm.com"
    val ip = "localhost"

    val i =
      implicitly[JSONConvertable[GetAllEntityiesForUser]]

    lazy val owner: RefToEntityOwningUser =
      RefToEntityOwningUser.makeFromRef(
        UserLoginStatusHandler.getUserLoginStatusDev.userOption.get.ref
      )

    lazy val q = GetAllEntityiesForUser(owner, None)

    val j: Json = q.asJson

    import io.circe.syntax._
    //import io.circe.generic.JsonCodec
    import io.circe.generic.auto._

    Ajax
      .post(s"http://$ip:8080/GetAllEntityiesForUser",
            j.spaces4,
            headers = headers)
      .map(_.responseText).map(i.fromJSONToObject(_)).onComplete(
        (x: Try[GetAllEntityiesForUser]) => {
          val res1: UserMap = x.toOption.get.res.get

          Cache.streamToSetInitialCacheState.send(res1)
          println(res1)

          Cache.user.cellLoop
            .listen(
              (cacheMap: CacheMap[User]) =>
                println(s"updated cacheMap:${cacheMap}")
            )
        }
      )

  }
}
