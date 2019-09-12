export NODE_PATH=./node/node_modules
sbt "testOnly *AsyncRequestTest  -- -z getentity"
sbt "testOnly *AsyncRequestTest  -- -z getAllUserRefs"
#sbt "testOnly *AsyncRequestTest"
