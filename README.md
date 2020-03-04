# DScript
A new script language based on Java

## Features
### Assign a variable
``` javascript
var a=12;
```
The translation engine will recognize the type of the value automatically.This is just like JavaScript,which can help you simplify your assignment.

### Conditional branching
``` javascript
var a=12;
var b=23;
if(a<b){
  System.output("a is smaller than b");
}
else
{
  System.output("a is bigger than or equals b");
};
unless(a<b){
  System.output("a is bigger than or equals b");
};
```
Added a new keyword ``unless(...)`` for ``if(!...)``

### Better ternary operator
``` javascript
var a=25;
var b=13;
var c=45;
System.output(a>b?1:0); // 1
System.output(a>c?1:0); // 0
System.output(a>b?1); // 1
System.output(a>c?1); // 25
``` 
You can even remove ':'.In this case,if true,it returns the value behind '?'.If false,it returns the left value of the condition.

### XOR
``` javascript
System.output(12>5##3==6); //true
System.output(12>5##3!=6); //false
System.output(12<5##3==6); //false
```
Added '##' as XOR that compares the two boolean values beside it,returns true if they are differnt and returns false if not.

### More features are coming soon!
