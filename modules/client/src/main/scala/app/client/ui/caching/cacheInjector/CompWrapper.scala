package app.client.ui.caching.cacheInjector

import japgolly.scalajs.react.{CtorType, ScalaComponent}

case class CompWrapper[Props, State, Backend]
(
  cache: CacheInterface,
  propsProvider: () => Props,
  comp: ScalaComponent[
    CacheInterfaceWrapper[Props],
    State,
    Backend,
    CtorType.Props]
) {

  val wrapped_cachTestRootComp =
    new CacheInjectorHOC[Backend, Props, State](comp)

  val ciw: CacheInterfaceWrapper[Props] =
    CacheInterfaceWrapper[Props](
      cacheInterface = cache,
      props = propsProvider()
    )

  val wr = wrapped_cachTestRootComp.wrapperConstructor(ciw)

}
