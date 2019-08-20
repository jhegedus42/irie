#!/usr/bin/env bash
export NODE_PATH=./node/node_modules:$NODE_PATH
sbt 'clean'
sbt 'compile'
sbt 'server/testOnly app.server.httpServer.PersistenceModuleTest'
