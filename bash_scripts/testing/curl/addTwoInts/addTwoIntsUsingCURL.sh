URL="route_SumIntPostRequest_void_auto_generated"
echo 'request:'
echo
cat addTwoInts.json
echo 'response:'
echo
curl -d "@addTwoInts.json" -H "Content-Type: application/json" -X POST http://localhost:8080/$URL
echo 
