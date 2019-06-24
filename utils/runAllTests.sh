#export NODE_PATH=./node/node_modules:$NODE_PATH

# testing server side code:
sbt 'clean'
sbt 'test' 
