#export NODE_PATH=./node/node_modules:$NODE_PATH

# testing server side code:
sbt 'clean'
sbt 'layer_W_JVM_akka_http_server/test' 
sbt 'layer_X_JVM_stateAccess/test' 
sbt 'layer_Y_JVM_persistence/test' 
sbt 'layer_Z_JVM_and_JS_sharedJVM/test' 
sbt 'layer_Z_JVM_and_JS_sharedJS/test'
