package app.server.httpServer.routes.post

import akka.http.scaladsl.server.Route
import app.shared.comm.PostRequest

private[post] case class PostRoute[Req <: PostRequest](route: Route )
