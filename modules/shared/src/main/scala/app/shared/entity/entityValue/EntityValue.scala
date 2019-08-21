package app.shared.entity.entityValue
import io.circe.generic.auto._

/**
  *
  * This is the supertype of the possible types of the value
  * which an entity can contain. Every entity contains only
  * one value.
  *
  * @tparam T
  */

trait EntityValue[T<:EntityValue[T]]








