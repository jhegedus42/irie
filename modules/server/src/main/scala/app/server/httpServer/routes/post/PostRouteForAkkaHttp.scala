package app.server.httpServer.routes.post

import akka.http.scaladsl.server.Route
import app.shared.comm.PostRouteType

private[post] case class PostRouteForAkkaHttp[Req <: PostRouteType](route: Route)
