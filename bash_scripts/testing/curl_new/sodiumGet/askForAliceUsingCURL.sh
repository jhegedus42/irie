#!/usr/bin/env bash
URL="GetAllEntityiesForUser"

curl -d "@askForAlice.json" -H "Content-Type: application/json" -X POST http://commserver.asuscomm.com:8080/$URL
