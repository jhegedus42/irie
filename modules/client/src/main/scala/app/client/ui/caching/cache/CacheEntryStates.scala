package app.client.ui.caching.cache

import app.shared.comm.{PostRequest, PostRequestType}

object CacheEntryStates {
  sealed trait CacheEntryState[RT<:PostRequestType, Req<: PostRequest[RT]] {
    def isLoading: Boolean =
      this match {
        case Loading( _ )   => true
        case Loaded( _, _ ) => false
      }

    def toOption: Option[Req#ResT] =
      this match {
        case Loading( _ )     => Option.empty
        case Loaded( _, res ) => Some(res)
      }
  }
  case class Loading[RT<:PostRequestType, Req<: PostRequest[RT]](param: Req#ParT ) extends CacheEntryState[RT,Req]

  case class Loaded[RT<:PostRequestType, Req<: PostRequest[RT]](param:  Req#ParT, result: Req#ResT )
      extends CacheEntryState[RT,Req]

  case class Stale[RT<:PostRequestType, Req<: PostRequest[RT]](param:  Req#ParT, result: Req#ResT )
    extends CacheEntryState[RT,Req]

}
