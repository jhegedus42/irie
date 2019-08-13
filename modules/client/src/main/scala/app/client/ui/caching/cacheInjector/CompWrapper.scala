package app.client.ui.caching.cacheInjector

import japgolly.scalajs.react.{CtorType, ScalaComponent}

trait ToBeWrappedComponent[Comp]{
  type Props
  type State
  type Backend
}


// todo-one-day type projector - use this thing below
//  instead of the one up there ^^^
case class CompWrapper2[Comp<:ToBeWrappedComponent[Comp]]
(
  cache: CacheInterface,
  propsProvider: () => Comp#Props,
  comp: ScalaComponent[
    CacheInterfaceWrapper[Comp#Props],
    Comp#State,
    Comp#Backend,
    CtorType.Props]
) {

  val wrapped_cachTestRootComp =
    new CacheInjectorHOC[Comp#Backend, Comp#Props, Comp#State](comp)

  val ciw: CacheInterfaceWrapper[Comp#Props] =
    CacheInterfaceWrapper[Comp#Props](
      cacheInterface = cache,
      props = propsProvider()
    )

  val wr = wrapped_cachTestRootComp.wrapperConstructor(ciw)

}
