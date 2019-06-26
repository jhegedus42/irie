export NODE_PATH=./node/node_modules:$NODE_PATH
echo
echo "---------------------------------------------------------------------------------------"
echo "Please make sure that a server is running before running this client side test,"
echo "for example by running ./utils/runTestServerInSbt_withCleaning.sh in an other terminal."
echo "---------------------------------------------------------------------------------------"
echo
sbt layer_V_JS_client/test

echo
echo
echo "----------------------------------"
echo "now we build the .js file that will run in the browser, ultimately"
echo
echo

sbt layer_V_JS_client/fastOptJS


