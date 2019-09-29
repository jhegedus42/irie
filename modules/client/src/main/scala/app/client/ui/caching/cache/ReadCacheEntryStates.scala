package app.client.ui.caching.cache

import app.shared.comm.{PostRequest, PostRequestType, ReadRequest}

object ReadCacheEntryStates {
  sealed trait ReadCacheEntryState[
    RT  <: ReadRequest,
    Req <: PostRequest[RT]] {

    def isLoading: Boolean =
      this match {
        case InFlight(_) => true
        case _           => false
      }

    def isFresh: Boolean =
      this match {
        case Returned(_, _) => true
        case _              => false
      }

    /**
      * @return None if loading, Some if Stale or Loaded.
      */
    def toOption: Option[Req#ResT] =
      this match {
        case InFlight(_)      => Option.empty
        case Returned(_, res) => Some(res)
        case Stale(_, res)    => Some(res)
      }

    def toOptionEither: Option[Either[Req#ResT, Req#ResT]] =
      this match {
        case InFlight(_)      => Option.empty
        case Returned(_, res) => Some(Right(res))
        case Stale(_, res)    => Some(Left(res))
      }

  }

  /**
    *
    * This denotes the state when an AJAX request is "in the air", it has been sent but
    * has not returned yet. All other states, at the present moment, are "not-in-flight"
    * type of states. They have either 1) Returned, 2) TimedOut, 3) ReturnedWithError
    *
    *
    * @param param
    * @tparam RT
    * @tparam Req
    */
  case class InFlight[RT <: ReadRequest, Req <: PostRequest[RT]](
    param: Req#ParT)
      extends ReadCacheEntryState[RT, Req]

  case class Returned[RT <: ReadRequest, Req <: PostRequest[RT]](
    param:  Req#ParT,
    result: Req#ResT)
      extends ReadCacheEntryState[RT, Req]

  case class Stale[RT <: ReadRequest, Req <: PostRequest[RT]](
    param:  Req#ParT,
    result: Req#ResT)
      extends ReadCacheEntryState[RT, Req]

  case class TimedOut[RT <: ReadRequest, Req <: PostRequest[RT]](
    param: Req#ParT)
      extends ReadCacheEntryState[RT, Req]

  /**
    *
    * A general type of state that indicates that something went wrong.
    * This can be error that
    *     1) arose inside the server, during processing a request
    *     2) authorization/authentication error
    *     3) "route not exist, etc..."-type of error :
    *        these are errors that arose already before the request
    *        reaches the request handling logic, so the request
    *        bounces back/off already on the outermost layer of the
    *        webservice/server which is the "http/routing-layer"
    *        (in the implementation of irie it is the akka-http
    *        routing code)
    *
    * @param param
    * @param descriptionOfError
    * @tparam RT
    * @tparam Req
    */
  case class ReturnedWithError[RT <: ReadRequest, Req <: PostRequest[RT]](
    param: Req#ParT, descriptionOfError: String)
      extends ReadCacheEntryState[RT, Req]

}