#!/usr/bin/env bash
sbt 'clean'
sbt 'update'
sbt 'compile'
sbt 'fastOptJS'
server/runMain app.server.Main
