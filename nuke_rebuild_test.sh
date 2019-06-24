./utils/super_clean.sh
./utils/get_node_packages_and_create_js_bundle.sh
export NODE_PATH=./node/node_modules:$NODE_PATH
./utils/runAllTests.sh

