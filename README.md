

# Getting Started

- Step 0 : Install the following build tools: 

  - node    
    - version 6.6.0 or higher 
  - [yarn](https://yarnpkg.com/en/)    
    - version 0.20.3 or higher 
  - [sbt](https://www.scala-sbt.org/)     
    - version 0.13.17 
  - git 
    - version 2.4.2 or higher

- Step 1: execute the following bash commands:

```
> git clone https://github.com/jhegedus42/IM_shared_2018_11_22
> cd IM_shared_2018_11_22
> cd node
> yarn
> ./node_modules/.bin/webpack
> cd ..
> utils/runTestServerInSbt_withCleaning.sh
```

- Step 2: Open the browser at `http://localhost:8043/`

