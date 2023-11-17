[back](README.md)

# How Skippy Works

Skippy improves test efficiency with a two-pronged approach: 
- [a powerful build plugin](https://github.com/skippy-io/skippy-gradle/blob/main/skippy-gradle-plugin/README.md#skippy-gradle-plugin) and 
- [a smart JUnit extension](https://github.com/skippy-io/skippy-junit5/blob/main/README.md#skippy-junit5). 

The build plugin harnesses the power of JaCoCo's coverage analysis and ASM's bytecode instrumentation to capture 
detailed execution data for individual test. In addition, the plugin captures hash for each source and class file
in your project. Then, Skippy's JUnit extension takes over, analyzing project changes to identify the tests that
are affected by recent updates.


The result? A streamlined test suite that only runs what's needed.

Check out [Getting Started with Skippy, Gradle & JUnit 5](tutorials/getting-started-with-gradle-and-junit5/README.md) to
learn more.