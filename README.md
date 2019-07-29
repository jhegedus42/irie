

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
> ./nuke_rebuild_test.sh 
```

- Step 2: If the tests went OK, then 
   - start `runTestServerInSbt_light_cleaning.sh` in the terminal
   - and open the browser at `http://localhost:8080/`

- Step 3: Be amazed ! It's a completely useless application. <br>
          It does absolutely nothing. Except for "running" and displaying <br>
          a few "Lorem ipsum dolor sit amet, consectetur adipiscing elit,..." <br> 
          inspired pages.


---
# The "easy" way :
 
Intermittently, I am running the server on my laptop, <br> 
          and at those times the running app can be viewed at <br>
          http://commserver.asuscomm.com:8080/ <br>
          or at (if the above one is not working) :<br>
          http://84.248.82.193:8080/
