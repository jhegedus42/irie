export NODE_PATH=./node/node_modules:$NODE_PATH
sbt 'clean'
sbt 'compile'
sbt 'test'
