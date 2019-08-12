#!/usr/bin/env bash
sbt 'clean'
sbt 'update'
sbt 'compile'
sbt 'fastOptJS'
sbt 'server:runMain app.server.Main "192.168.2.50"'
