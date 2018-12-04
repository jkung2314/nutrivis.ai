# Basic Style Guide

Consisting of exerpts from [this page](https://github.com/raywenderlich/java-style-guide). When in doubt, refer to this page.

A general note: `BE VERY FUCKING CAREFUL WHEN MESSING WITH GRADLE`

### Brace Style

Only trailing closing-braces are awarded their own line. All others appear the
same line as preceding code:

__BAD:__

```java
class MyClass
{
  void doSomething()
  {
    if (someTest)
    {
      // ...
    }
    else
    {
      // ...
    }
  }
}
```

__GOOD:__

```java
class MyClass {
  void doSomething() {
    if (someTest) {
      // ...
    } else {
      // ...
    }
  }
}
```

Conditional statements are always required to be enclosed with braces,
irrespective of the number of lines required.

__BAD:__

```java
if (someTest)
  doSomething();
if (someTest) doSomethingElse();
```

__GOOD:__

```java
if (someTest) {
  doSomething();
}
if (someTest) { doSomethingElse(); }
```

### Comments 

__BAD:__

```java
//nocaps or space at start of sentence
class MyClass { 
	//...
} 

class MyClass {  // Inline comments
	//...
	}

class MyClass { 
	//...
	}

```
The last example is no comments, also bad.

__GOOD:__

```java
// This is a class I created and it does this and that
class MyClass { 
	//...
	}
```
Wherever possible XML resource files should be used:

- Strings => `res/values/strings.xml`
- Styles => `res/values/styles.xml`
- Colors => `res/color/colors.xml`
- Animations => `res/anim/`
- Drawable => `res/drawable`








