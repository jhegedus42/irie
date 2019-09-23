package app.server.httpServer.routes.post

import akka.http.scaladsl.server.Route
import app.shared.comm.{PostRequest, PostRequestType}

private[post] case class PostRoute[RT<:PostRequestType, Req<: PostRequest[RT]](route: Route)
