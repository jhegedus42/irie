package client.cache.comm

import client.cache.Cache
import client.ui.login.UserLoginStatusHandler
import comm.crudRequests.JSONConvertable
import comm.crudRequests.persActorCommands.GetAllEntityiesForUser
import dataStorage.{
  Ref,
  RefToEntityOwningUser,
  TypedReferencedValue,
  User
}
import dataStorage.stateHolder.UserMap
import org.scalajs.dom.ext.Ajax
import testingData.TestEntitiesForUsers

import scala.collection.immutable.HashMap
import io.circe.Decoder.Result
import io.circe._
import io.circe.Json
import io.circe.syntax._
import io.circe.generic.JsonCodec
import io.circe.generic.auto._
import io.circe.parser._
import shapeless.Typeable

import scala.concurrent.ExecutionContextExecutor

object TestAjaxRequest {

  def populateUserEntityCache()
//                             (
//    implicit
//    a: HashMap[Ref[User], TypedReferencedValue[User]]
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

    Ajax
      .post(s"http://$ip:8080/GetAllEntityiesForUser",
            j.spaces4,
            headers = headers)
      .map(_.responseText).map(i.getObject(_)).onComplete(
        x => {
          val res1: UserMap = x.toOption.get.res.get
          Cache.streamToSetInitialCacheState.send(res1)
          println(res1)
          Cache.user.cellLoop
            .listen(x => println(s"udate:${x}"))
        }
      )

  }
}
