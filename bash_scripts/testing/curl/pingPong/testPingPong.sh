URL="ping_pong"

curl -d "@testPingPong.json" -H "Content-Type: application/json" -X POST http://localhost:8080/$URL
