console.oldTrace=console.trace
f =  function(x) {
     console.groupCollapsed(x);
     console.oldTrace(x);
     console.groupEnd()
};

console.trace=f