URL="route_SumIntPostRequest_void_auto_generated"
IP="192.168.2.50"
echo 'request:'
echo
cat addTwoInts.json
echo 'response:'
echo
curl -d "@addTwoInts.json" -H "Content-Type: application/json" -X POST http://$IP:8080/$URL
echo 
