package app.server.httpServer.routes.post

import akka.http.scaladsl.server.Route
import app.shared.comm.{PostRequest, PostRequestType}

private[post] case class PostRoute[Req<: PostRequest[_]](route: Route)
