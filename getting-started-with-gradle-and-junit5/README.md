[back](../../README.md)

# Getting Started with Skippy, Gradle & JUnit 5

## Setting Up Your Environment

Begin by cloning the skippy-docs repository:
```
git clone git@github.com:skippy-io/skippy-docs.git
```

Then, move into the tutorial directory:
```
cd skippy-docs/tutorials/getting-started-with-gradle-and-junit5/
```

Ensure that the project builds successfully:
```
./gradlew build clean
```

A successful build will display:
```
BUILD SUCCESSFUL
```

## Exploring the Codebase

Let's take a quick look at the codebase.

### The build.gradle File

`build.gradle` applies the `io.skippy` plugin and adds a dependency to `skippy-junit5`: 

```groovy
plugins {
    id 'io.skippy' version '0.0.6'
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation 'io.skippy:skippy-junit5:0.0.6'
}
```

The plugin adds a couple of tasks that we will use throughout the tutorial:
```
./gradlew tasks
```
Output:
 
```
...

Skippy tasks
------------
skippyAnalyze
skippyClean

...
```

Note: You can play around with those tasks. If you do so, execute
```
./gradlew clean skippyClean
```
before proceeding with the tutorial. 

### src/main/java 

The main source set contains three classes:

```
com
└─ example
   ├─ LeftPadder.java
   ├─ RightPadder.java
   └─ StringUtils.java
```

`StringUtils` is a utility class that provides methods for padding strings:
```java
class StringUtils {

    static String padLeft(String input, int size) {
        // method logic
    }

    static String padRight(String input, int size) {
        // method logic
    }
}
```

`LeftPadder` and `RightPadder` utilize `StringUtils` for their functionality: 
```java
class LeftPadder {

    static String padLeft(String input, int size) {
        return StringUtils.padLeft(input, size);
    }

}
```
```java
class RightPadder {

    static String padRight(String input, int size) {
        return StringUtils.padRight(input, size);
    }

}
```

### src/test/java

The test source set contains three tests and one class that stores constants:
```
com
└─ example
   ├─ LeftPadderTest.java
   ├─ RightPadderTest.java
   ├─ StringUtilsTest.java
   └─ TestConstants.java
```

`LeftPadderTest` and `RightPadderTest` are unit tests for their respective classes:

```java
import io.skippy.junit5.Skippified;

@Skippified
public class LeftPadderTest {

    @Test
    void testPadLeft() {
        var input = TestConstants.HELLO;
        assertEquals(" hello", LeftPadder.padLeft(input, 6));
    }

}
```
Note: We will refer to tests that are annotated with `@Skippified` as skippified tests.

`StringUtilsTest` tests the `StringUtil` class and is a standard (e.g., non-skippified) JUnit test:
```java
public class StringUtilsTest {

    @Test
    void testPadLeft() {
        var input = TestConstants.HELLO;
        assertEquals(" hello", StringUtils.padLeft(input, 6));
    }

    @Test
    void testPadRight() {
        var input = TestConstants.HELLO;
        assertEquals("hello ", StringUtils.padRight(input, 6));
    }

}
```

`TestConstants` declares a string constant:
```java
class TestConstants {
    static final String HELLO = "hello";
}
```


## Run The Tests

Run the tests:
```
./gradlew clean test 
```

The output should resemble:

```
DEBUG i.s.c.SkippyAnalysis - com.example.LeftPadderTest: No coverage data found: Execution required
LeftPadderTest > testPadLeft() PASSED

DEBUG i.s.c.SkippyAnalysis - com.example.RightPadderTest: No coverage data found: Execution required
RightPadderTest > testPadRight() PASSED

StringUtilsTest > testPadLeft() PASSED
StringUtilsTest > testPadRight() PASSED
```

Skippy did not find data in the skippy folder to decide whether `LeftPadderTest` or `RightPadderTest` need to be 
executed.  In this case, Skippy will always execute skippified tests. 

Also note that there is no Skippy-specific logging for `StringUtilsTest`: It's a non-skippified test.

## Run the skippyAnalyze task

Run the `skippyAnalyze` task to trigger a Skippy analysis:

```
./gradlew skippyAnalyze
```

You should see something like this:
```
> Task :skippyAnalyze
Capturing coverage data for com.example.LeftPadderTest in skippy/com.example.LeftPadderTest.csv
Capturing coverage data for com.example.RightPadderTest in skippy/com.example.RightPadderTest.csv
Creating the Skippy analysis file skippy/analyzedFiles.txt.
```

__Note__: You can skip to the next section if you don't care about how Skippy works under the hood.

`skippyAnalyze` generates a bunch of files in the `skippy` folder:

```
ls -l skippy
```
Output:
```
com.example.LeftPadderTest.csv
com.example.RightPadderTest.csv
analyzedFiles.txt
```

Let's take a look at `LeftPadderTest.csv`: 
```
...     PACKAGE,        CLASS,              ...     LINES_COVERED,    ...
...     com.example,    TestConstants,      ...     0,                ...
...     com.example,    StringUtils,        ...     3,                ...
...     com.example,    RightPadder,        ...     0,                ...
...     com.example,    LeftPadder,         ...     1,                ...
...     com.example,    RightPadderTest,    ...     0,                ...
...     com.example,    LeftPadderTest,     ...     4,                ...
...     com.example,    StringUtilsTest,    ...     0,                ...
```

The file contains a [JaCoCo](https://www.jacoco.org/) coverage report for `LeftPadderTest`. According to JaCoCo, 
`LeftPadderTest` covers lines in the following classes: 
- `StringUtils`
- `LeftPadder`
- `LeftPadderTest`

You might wonder: Shouldn't there be line coverage for the `TestConstants` class? Yes. But: JaCoCo's analysis is based 
on the  execution of instrumented bytecode. Since the Java compiler inlines the value of `TestConstants.HELLO` into 
`LeftPadderTest`'s class file, JaCoCo has no way to detect this. 

Don't worry - Skippy got you covered! Skippy combines JaCoCo's dynamic bytecode analysis with  a custom, static bytecode 
analysis to detect relevant changes. To do this, it needs additional information that is stored in  `analyzedFiles.txt`:

```
build/classes/java/main/com/example/LeftPadder.class:9U3+WYit7uiiNqA9jplN2A==
build/classes/java/test/com/example/LeftPadderTest.class:3KxzE+CKm6BJ3KetctvnNA==
build/classes/java/main/com/example/RightPadder.class:ZT0GoiWG8Az5TevH9/JwBg==
build/classes/java/test/com/example/RightPadderTest.class:naR4eGh3LU+eDNSQXvsIyw==
build/classes/java/main/com/example/StringUtils.class:4VP9fWGFUJHKIBG47OXZTQ==
build/classes/java/test/com/example/StringUtilsTest.class:p+N8biKVOm6BltcZkKcC/g==
build/classes/java/test/com/example/TestConstants.class:3qNbG+sSd1S1OGe0EZ9GPA==
```
The file contains hashes for all classes in the project.

Now, let's see what Skippy can do with this data.

## Re-Run The Tests

Re-run the tests:
```
./gradlew test                 
```

You should see something like this:
```
DEBUG i.s.c.m.SkippyAnalysisResult - com.example.LeftPadderTest: No changes in test or covered classes detected. Execution skipped.
LeftPadderTest > testPadLeft() SKIPPED

DEBUG i.s.c.m.SkippyAnalysisResult - com.example.RightPadderTest: No changes in test or covered classes detected. Execution skipped.
RightPadderTest > testPadRight() SKIPPED

... output for non-skippified tests ...

```

Skippy compares the current state of the project with the analysis in the `skippy` folder. It detects that both 
`LeftPadderTest` and `RightPadderTest` can be skipped:

- There was no change in either `LeftPadderTest` or `RightPadderTest`.
- There was no change in any of the covered classes.

## Testing After Modifications

When changes are made, Skippy reassesses which tests to run based on it's bytecode based change detection. Reasoning 
based on the bytecode is powerful: It allows Skippy to distinguish relevant changes (e.g., new or updated instructions)
from irrelevant ones (e.g., a change in a 
[LineNumberTable attribute](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.7.12) due to the
addition of a line break somewhere in the source file).

Let's perform some experiments.

### Experiment 1

Add a comment to `StringUtils`:

```java
/**
 * New class comment.
 */
class StringUtils {
        
    ...
}
```

Re-run the tests:
```
./gradlew test
```

Despite the newly added comment, Skippy detects no significant changes. `LeftPadderTest` and 
`RightPadderTest` will be skipped:
```
DEBUG i.s.c.SkippyAnalysis - com.example.LeftPadderTest: No changes in test or covered classes detected. Execution skipped.
LeftPadderTest > testPadLeft() SKIPPED

DEBUG i.s.c.SkippyAnalysis - com.example.RightPadderTest: No changes in test or covered classes detected. Execution skipped.
RightPadderTest > testPadRight() SKIPPED

...
```

### Experiment 2

Undo the changes from the previous experiment:
```
git stash
```

Comment out the first three lines of `StringUtils#padLeft`:

```java
class StringUtils {
    
    static String padLeft(String input, int size) {
//        if (input.length() < size) {
//            return padLeft(" " + input, size);
//        }
        return input;
    }
}
```

Re-run the tests:
```
./gradlew test
```

Skippy detects the change and runs the skippified tests again:
```
DEBUG i.s.c.SkippyAnalysis - com.example.LeftPadderTest: Bytecode change in covered class 'com.example.StringUtils' detected. Execution required.
expected: < hello> but was: <hello>
LeftPadderTest > testPadLeft() FAILED

DEBUG i.s.c.SkippyAnalysis - com.example.RightPadderTest: Bytecode change in covered class 'com.example.StringUtils' detected. Execution required.
RightPadderTest > testPadRight() PASSED

...
```

Note that at this point in time, Skippy executes a test if the covered class contains a significant bytecode change 
(e.g., new or updated instructions). The test itself may or may not depend on this change. In the above example, 
`RightPadderTest` could be skipped as well. 

While we plan to implement more granular change detection in the future, we currently apply the 80/20 rule: Our focus 
is robustness and simplicity while providing a significant reduction in useless testing for applications that contain 
large quantities of source files and tests.

### Experiment 4

Undo the changes from the previous experiment:
```
git stash
```

Now, let's see what happens if you change the expected value in `LeftPadderTest` from ` hello` to `HELLO`:
```java
@ExtendWith(Skippy.class)
public class LeftPadderTest {

    @Test
    void testPadLeft() {
        var input = TestConstants.HELLO;
        // assertEquals(" hello", LeftPadder.padLeft(input, 6));
        assertEquals(" HELLO", LeftPadder.padLeft(input, 6));
    }

}
```

Re-run the tests:
```
./gradlew test
```

Skippy detects the change and runs `LeftPadderTest`again:
```
DEBUG i.s.c.SkippyAnalysis - com.example.LeftPadderTest: Bytecode change detected. Execution required.
expected: < HELLO> but was: < hello>
LeftPadderTest > testPadLeft() FAILED

DEBUG i.s.c.SkippyAnalysis - com.example.RightPadderTest: No changes in test or covered classes detected. Execution skipped.
RightPadderTest > testPadRight() SKIPPED

...
```

### Experiment 4

Undo the changes from the previous experiment:
```
git stash
```

Lastly, let's see what happens if you change the value of the constant in `TestConstants`:

```java
class TestConstants {

    // static final String HELLO = "hello";
    static final String HELLO = "bonjour";

}
```

Re-run the tests:

```
./gradlew test
```

Skippy detected the change and re-runs both skippified tests:
```
DEBUG i.s.c.m.SkippyAnalysisResult - com.example.LeftPadderTest: Bytecode change detected. Execution required.
expected: < hello> but was: <bonjour>
LeftPadderTest > testPadLeft() FAILED

DEBUG i.s.c.m.SkippyAnalysisResult - com.example.RightPadderTest: Bytecode change detected. Execution required.
expected: <hello > but was: <bonjour>
RightPadderTest > testPadRight() FAILED

...
```

Congratulations! You've successfully integrated Skippy into your project, ensuring that only necessary tests are run, 
saving you time and resources.