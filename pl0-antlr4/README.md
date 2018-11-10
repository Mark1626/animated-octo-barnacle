## PL/0 Antlr4 Java Target

An implementation of PL/0 using antlr4

## Building from source

You will need to have [Apache Ant](https://ant.apache.org/index.html) and [Apache Ivy](http://ant.apache.org/ivy/) installed

Create build with
```
  ant build
```

The executable jar will be present in `dist/`

## Usage

Java Runtime JDK 1.8
```
  java -jar PLInterpretor.jar path/to/file
```


#### Third Party Libraries used
- [Apache Ant](https://ant.apache.org/index.html), [Apache Ivy](http://ant.apache.org/ivy/) build generation
- [Antlr4](https://www.antlr.org) parser generator
- [Junit 4.12 ](https://junit.org/junit4/) testing
- [CheckStyle](https://junit.org/junit4/)
- [FindBugs](https://github.com/findbugsproject/findbugs)

#### To-Do
- CI with [TravisCI](https://travis-ci.org)
