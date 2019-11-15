package client

import client.cache.NormalizedStateHolder
import comm.crudRequests.{GetAllEntityiesForUser, JSONConvertable}
import dataStorage.RefToEntityOwningUser
import dataStorage.stateHolder.UserMap
import io.circe.Json
import org.scalajs.dom.ext.Ajax
import testingData.TestEntitiesForUsers

import scala.concurrent.ExecutionContextExecutor

object TestAjaxRequest {

  def query(): Unit = {

    implicit def executionContext: ExecutionContextExecutor =
      scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

    import io.circe.syntax._

    val headers: Map[String, String] = Map(
            "Content-Type" -> "application/json"
    )

    val ip = "commserver.asuscomm.com"

    val i = implicitly[JSONConvertable[GetAllEntityiesForUser]]

    val owner: RefToEntityOwningUser =
      RefToEntityOwningUser.makeFromRef(
              TestEntitiesForUsers.aliceEntity.ref
      )

    val q = GetAllEntityiesForUser(owner, None)

    val j: Json = q.asJson

    Ajax
      .post(s"http://$ip:8080/GetAllEntityiesForUser",
            j.spaces4,
            headers = headers)
      .map(_.responseText).map(i.getObject(_)).onComplete(x => {
        val res1: UserMap = x.toOption.get.res.get
        NormalizedStateHolder.streamToSetInitialCacheState.send(res1)
        println(res1)
        NormalizedStateHolder.user.cell
          .listen(x => println(s"udate:$x"))
      })

  }
}
