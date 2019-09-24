package app.client.ui.caching.cache

import app.shared.comm.{PostRequest, PostRequestType, ReadRequest}



object CacheEntryStates {
  sealed trait CacheEntryState[RT<:ReadRequest, Req<: PostRequest[RT]] {
    def isLoading: Boolean =
      this match {
        case Loading( _ )   => true
        case Loaded( _, _ ) => false
      }

    def toOptionEither: Option[Either[Req#ResT,Req#ResT]] =
      this match {
        case Loading( _ )     => Option.empty
        case Loaded( _, res ) => Some(Right(res))
        case Stale( _, res )  => Some(Left(res))
      }

  }
  case class Loading[RT<:ReadRequest, Req<: PostRequest[RT]](param: Req#ParT ) extends CacheEntryState[RT,Req]

  case class Loaded[RT<:ReadRequest, Req<: PostRequest[RT]](param:  Req#ParT, result: Req#ResT )
      extends CacheEntryState[RT,Req]

  case class Stale[RT<:ReadRequest, Req<: PostRequest[RT]](param:  Req#ParT, result: Req#ResT )
    extends CacheEntryState[RT,Req]


}
