

# Getting Started, the "difficult" way
- Step 0 : Install the following build tools: 

  - `node.js`  
  - [yarn](https://yarnpkg.com/en/)    
  - [sbt](https://www.scala-sbt.org/)     
  - `git`

More specifically, for me, building and running the app, using `node v12.7.0`, `yarn 1.17.3`, `git 2.20.1 (Apple Git-117)` on `OSX Mojave 10.14.5` worked fine.

- Step 1: execute the following bash commands:

```
> git clone https://github.com/jhegedus42/irie
> cd irie
> ./bash_scripts/building/build_js_dep_bundle.sh
> ./bash_scripts/running/run_on_localhost.sh
```

- Step 2: open the browser at `http://localhost:8080/`

---
# The "easy" way :
 
Intermittently, I am running the server on my laptop, <br> 
          and at those times the running app can be viewed at <br>
          http://commserver.asuscomm.com:8080/ <br>


# Ackowledgement


<div style="center {margin:auto}" align="center">
<a href="https://www.jetbrains.com/"><img src="https://github.com/jhegedus42/irie/blob/f455e53cbd2ed7bfb68a079107eb29daeddbee07/jetbrains.png?raw=true"  width="300"  ></a>
</div>


The development of this open source project has been kindly supperted 
by [JetBrains](https://www.jetbrains.com/). 
They offer [free open source licenses](https://www.jetbrains.com/community/opensource/) 
for helping the developers of actively developed open-source, non-commercial products, 
which meet the [open source defitition](https://opensource.org/docs/osd). 
In my case this project is licensed under [MIT license](https://opensource.org/licenses/MIT).

I am now able to use IntelliJ Ultimate version which 
has [built in support for Scala.js](https://plugins.jetbrains.com/plugin/1347-scala/) 
in which large portion of this project is written.
Their support is gratelfully appreciated. 

