echo "First we will do a super deep cleaning."
echo "see './utils/super_clean.sh' to be shocked"
echo "how deep this cleaning is - Duke Nukem on"
echo "steroids."
echo
echo 
sleep 5
echo
echo 'Cleaning starts NOW:'
echo
./utils/super_clean.sh
echo
echo
echo 'Cleaning has ended.'
echo
echo
echo "Next, node packages will be installed and"
echo "JS dependencies for the client side will be"
echo "bundled up using webpack."
echo
echo
echo
./utils/get_node_packages_and_create_js_bundle.sh
echo
echo
echo "Node packages have been installed."
echo "Webpack-JS bundle has been created."
echo "(If everything went well.)"
echo
export NODE_PATH=./node/node_modules:$NODE_PATH
echo
echo
echo "Now lot of server side only unit tests will be"
echo "executed."
echo
echo
./utils/runUnitTestsForServer.sh
echo
echo
echo "Unit tests for the server are now completed".
echo
echo
echo "Next, I will do the unit tests for the client."
echo "These are perhaps better called as 'integration"
echo "tests', for this both an emulated client and"
echo "a test-server needs to be running started inside"
echo "two separate sbt processes."
echo
echo
echo
sleep 10 
echo
echo
echo
echo "-------------------------------------------"
echo "Starting test-server (also waiting 20 seconds for it to start up)"
echo "i.e. : we do not start the tests for the client for another"
echo "20 seconds."
echo
sbt 'layer_W_JVM_akka_http_server/test:runMain app.server.RESTService.mocks.runnableApps.TestServer_App_LabelOne_ClientTesting "localhost"' &
sleep 20
echo
echo "Waited 20 seconds for server to start, hopefully it did."
echo "If it did it says : elore iszunka medve borere."
echo "-------------------------------------------"
echo
echo "Now starting tests for client:"
echo
echo
sbt layer_V_JS_client/test
echo
echo "------------------------------"
echo
echo "Unit tests for client stopped running now."
echo "Waiting 5 seconds, just to be on safe side."
echo "Before cleaning things up...."
echo
echo
sleep 5
echo
echo "5 seconds are up."
echo 
echo "------------------------------"
echo 
echo "Your running processes now are:"
ps 
echo
echo 
echo "Now, I will be killing all sbt processes using"
echo "'killall -9 java'. to clean up resources, etc..."
echo "This might kill some of your other java processes too."
echo "That was not intended."
echo
sleep 5
echo
echo "'Killall -9 java' to be executed NOW."
echo
killall -9 java
echo
echo "'Killall -9 java' executed."
echo
echo
echo
echo "Your running processes after 'killall -9 java':"
ps
echo
sleep 5
echo
echo
echo "----------------------------------"
echo "now we build the .js file that will run in the browser, ultimately"
echo
echo

sbt layer_V_JS_client/fastOptJS

echo
echo "--------------------------------"
echo

echo "Press a few times enter. Just in case, to clean up"
echo "some unneccessary text in the terminal."
echo
echo
echo
echo "Also type 'ps' - just to see that all testing related"
echo "processes have been properly terminated and related"
echo "resources freed."
echo
echo
slepp 5
echo "Now, you can start the test server by:"
echo "./utils/runTestServerInSbt_light_cleaning.sh"
echo
echo
echo
echo "and open 'http://localhost:8080' in the browser"
