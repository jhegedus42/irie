

# Getting Started, the "right" way
- Step 0 : Install the following build tools: 

  - `node.js`  
  - [yarn](https://yarnpkg.com/en/)    
  - [sbt](https://www.scala-sbt.org/)     
  - `git`

More specifically, for me, building and running the app, using `node v12.3.1`, `yarn 1.16.0`, `git 2.20.1 (Apple Git-117)` on `OSX Mojave 10.14.4` worked fine.

- Step 1: execute the following bash commands:

```
> git clone https://github.com/jhegedus42/irie
> cd irie
> git checkout 2719f8ecc3faf224653f6595b3236eac04b20d6d
> ./bash_scripts_building/build_js_dep_bundle.sh
> ./run_on_localhost.sh
```

   

- Step 2: open the browser at `http://localhost:8080/`

---
# The "easy" way :
 
Intermittently, I am running the server on my laptop, <br> 
          and at those times the running app can be viewed at <br>
          http://commserver.asuscomm.com:8080/ <br>
          or at (if the above one is not working) :<br>
          http://84.248.82.193:8080/

---
# Some videos - from the past :
  2019 Aug 12 - https://www.youtube.com/watch?v=oZXtshCqNeM


---
# Setting the node path, for testing the client in `sbt`:
```
. ./bash/set_node_env_variable_for_testing.sh
env | grep NODE

```
the `.` in front of `./bash/set_node_env_variable_for_testing.sh` makes sure
that the exported environment variables "survive" the end of the 
execution of the `.sh` file.

The 
```
env | grep NODE
```
will check that the environment variable has been set correctly.

Once this done, you can test the `client side` by:
```
sbt
project client
test
```

Which should give you something like (in the `sbt` REPL):

```
sbt:root> project client
[info] Set current project to client (in build file:/Users/joco/dev/im/irie/)
sbt:client> test
I have just hacked the matrix
hello test world
[info] MainTest:
[info] - simple synchronous (blocking) - 'integration test' stub
[info] - url encoding / decoding
[info] Run completed in 298 milliseconds.
[info] Total number of tests run: 2
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 2, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 8 s, completed Aug 19, 2019 2:02:07 AM
sbt:client>

```
