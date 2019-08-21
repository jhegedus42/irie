package app.server.httpServer.routes.routeProviders.dynamicRouteProviders

import akka.http.scaladsl.server.Route
import app.shared.comm.PostRequest

private[routes] case class PostRoute[Req <: PostRequest](route: Route )
