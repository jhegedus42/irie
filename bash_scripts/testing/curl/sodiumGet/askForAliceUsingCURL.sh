#!/usr/bin/env bash
URL="GetAllEntityiesForUser"

curl -d "@askForAlice.json" -H "Content-Type: application/json" -X POST http://localhost:8080/$URL
