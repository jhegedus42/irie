rm out
rm err


sbt 'server/testOnly **.TypeSafeAccessToPersistentActorProviderTest' >out 2>err
sbt 'server/testOnly **.PersistentServiceProviderTest' >>out 2>>err
sbt 'server/testOnly **.RouteFactoryTest' >>out 2>>err

cat out | grep fail
cat err | grep fail

#sbt 'server/test'

