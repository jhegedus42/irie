package refs

/**
  *
  * This is the supertype of the possible types of the value
  * which an entity can contain. Every entity contains only
  * one value.
  *
  * @tparam T
  */
trait EntityType[+T <: EntityType[T]] {}
