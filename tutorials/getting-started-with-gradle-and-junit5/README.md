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
./gradlew build                      
```

A successful build will display:
```
BUILD SUCCESSFUL in 500ms
```

## Exploring the Codebase

Let's take a quick look at the codebase.

### The build.gradle File

`build.gradle` applies the Skippy plugin from Gradle's Plugin Portal:
```
plugins {
    id 'io.skippy' version '0.0.5'
}
```
The SkippyPlugin adds a couple of tasks that we will use throughout the tutorial:
```
./gradlew tasks
 
...

Skippy tasks
------------
skippyAnalysis
skippyClean
```

Additionally, it declares a dependency to `io.skippy:skippy-junit5` from Maven Central:
```
repositories {
    mavenCentral()
}

dependencies {
    testImplementation 'io.skippy:skippy-junit5:0.0.5'
}
```

### src/main/java 

This directory contains three classes:

```
com
 \-example
   |-LeftPadder.java
   |-RightPadder.java
   \-StringUtils.java
```

`StringUtils` is a utility class that provides methods for padding strings:
```
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
```
class LeftPadder {

    static String padLeft(String input, int size) {
        return StringUtils.padLeft(input, size);
    }

}
```
```
class RightPadder {

    static String padRight(String input, int size) {
        return StringUtils.padRight(input, size);
    }

}
```

### src/test/java

Here, we have three tests and one class that stores a constant that is used throughout the test suite:
```
com
 \-example
   |-LeftPadderTest.java
   |-RightPadderTest.java
   |-StringUtilsTest.java
   \-TestConstants.java
```

`TestConstants` declares a string constant:
```
class TestConstants {
    static final String HELLO = "hello";
}
```

`LeftPadderTest` and `RightPadderTest` are unit tests for their respective classes:

```
import io.skippy.junit5.Skippy;

@ExtendWith(Skippy.class)
public class LeftPadderTest {

    @Test
    void testPadLeft() {
        var input = TestConstants.HELLO;
        assertEquals(" hello", LeftPadder.padLeft(input, 6));
    }

}
```
Note: We will refer to tests that are annotated with `@ExtendWith(Skippy.class)` as skippified tests.


`StringUtilsTest` tests the `StringUtil` class and is a standard (e.g., non-skippified) JUnit test:
```
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

## Run The Test Suite

Execute the tests:
```
./gradlew clean skippyClean test 
```

`skippyClean` is used to clear previous analysis data in case you already played around with the `skippyAnalysis` task. 

The output should resemble:

```
LeftPadderTest
DEBUG i.s.c.m.SkippyAnalysisResult - com.example.LeftPadderTest: No analysis found. Execution required.
LeftPadderTest > testPadLeft() PASSED

RightPadderTest
DEBUG i.s.c.m.SkippyAnalysisResult - com.example.RightPadderTest: No analysis found. Execution required.
RightPadderTest > testPadLeft() PASSED

StringUtilsTest > testPadLeft() PASSED

StringUtilsTest > testPadRight() PASSED
```

Skippy has no historic execution data to decide whether `LeftPadderTest` or `RightPadderTest` test need to run. 
In this case, Skippy will always executes skippified tests. Also note that there is no Skippy-specific logging for 
`StringUtilsTest`: It's a non-skippified test.

## Execute the skippyAnalysis task

Run the `skippyAnalysis` task to trigger a Skippy analysis:

```
./gradlew skippyAnalysis
```

You should see something like this:
```
./gradlew skippyAnalysis

> Task :skippyCoverage_com.example.LeftPadderTest
Capturing coverage data for com.example.LeftPadderTest in skippy/com.example.LeftPadderTest.csv

> Task :skippyCoverage_com.example.RightPadderTest
Capturing coverage data for com.example.RightPadderTest in skippy/com.example.RightPadderTest.csv

> Task :skippyAnalysis
Capturing a snapshot of all source files in skippy/sourceSnapshot.md5
```

__Note__: You can skip to the next section if you don't care about how Skippy works under the hood.

`skippyAnalysis` generates a bunch of files in the `skippy` folder:

```
ls -l skippy

com.example.LeftPadderTest.csv
com.example.RightPadderTest.csv
sourceSnapshot.md5
```

Let's take a look at `LeftPadderTest.csv`: 
```
...     PACKAGE,        CLASS,              ...     INSTRUCTION_COVERED,    ...
...     com.example,    TestConstants,      ...     0,                      ...
...     com.example,    StringUtils,        ...     11,                     ...
...     com.example,    LeftPadder,         ...     3,                      ...
...     com.example,    RightPadder,        ...     0,                      ...
...     com.example,    RightPadderTest,    ...     0,                      ...
...     com.example,    LeftPadderTest,     ...     11,                     ...
...     com.example,    StringUtilsTest,    ...     0,                      ...
```

The file contains a JaCoCo coverage report for `LeftPadderTest`. According to JaCoCo, `LeftPadderTest` covers instructions 
in the following classes: 
- `StringUtils`
- `LeftPadder`
- `LeftPadderTest`

You might wonder: Shouldn't there be coverage for `TestConstants`? Yes. But: JaCoCo's analysis is based on the
execution of instrumented bytecode. Since the Java compiler inlines the value of `TestConstants.HELLO` into 
`LeftPadderTest`'s class file, JaCoCo has no way to detect that `LeftPadderTest` covers the constant in `TestConstants`. 

Don't worry - Skippy got you covered! Skippy combines JaCoCo's dynamic bytecode analysis with 
a custom, static bytecode analysis to detect changes both in source and bytecode. To do this, it needs 
additional information that is stored in  `sourceSnapshot.md5`:

```
com.example.StringUtils:        ../com/example/StringUtils.java:        ../com/example/StringUtils.class:       OUit8FjiK8bRBHkjssO9+Q==:   TB3Ri7NR47VGzsGKfSF6cg==
com.example.RightPadder:        ../com/example/RightPadder.java:        ../com/example/RightPadder.class:       lbQRvgnICPwJcg0ObY2wfA==:   FgPLN2IwhX2Y1n7TLYG9aw==
com.example.LeftPadder:         ../com/example/LeftPadder.java:         ../com/example/LeftPadder.class:        99PUNZm+uo4Rp5feNB5d/g==:   HeDsMUqerZxYhOi8+SyxHA==
com.example.TestConstants:      ../com/example/TestConstants.java:      ../com/example/TestConstants.class:     nK/HNeYLMeGZk5hlcPS8Yg==:   CjlZNllkdXvp5RozTW9ycQ==
com.example.LeftPadderTest:     ../com/example/LeftPadderTest.java:     ../com/example/LeftPadderTest.class:    tmeyvGT5uJAMQyQzbqbvyg==:   zEb0x7PQhzYAh00yZX50Wg==
com.example.RightPadderTest:    ../com/example/RightPadderTest.java:    ../com/example/RightPadderTest.class:   LfOMUnHmz0Gqv48PyG+Arw==:   pfL18c7B6SOZiFB+TsHpaw==
com.example.StringUtilsTest:    ../com/example/StringUtilsTest.java:    ../com/example/StringUtilsTest.class:   yq8CHRvmLIB5vb/eqkOlIw==:   KJg84+nME0Yh7uBsXwv9Vg==
```

The file contains 5 properties for each source file (both in the main and test source sets) in your project:

- the fully-qualified class name
- path of the source file
- path of the class file
- MD5 hash of the source file
- MD5 hash of the class file

In summary, `skippyAnalysis` captures the following data in the `skippy` folder of your project:
- Test coverage data for each skippified test.
- For each source file:
  - the fully qualified class name
  - the paths of the source and class file
  - the MD5 hashes of the source and class file

Now, let's see what Skippy can do with this data.

## Re-Run The Test Suite

Re-run the test suite:
```
./gradlew test                 
```

You should see something like this:
```
LeftPadderTest STANDARD_OUT
DEBUG i.s.c.m.SkippyAnalysisResult - com.example.LeftPadderTest: No changes in test or covered classes detected. Execution skipped.
LeftPadderTest > testPadLeft() SKIPPED

RightPadderTest STANDARD_OUT
DEBUG i.s.c.m.SkippyAnalysisResult - com.example.RightPadderTest: No changes in test or covered classes detected. Execution skipped.
RightPadderTest > testPadLeft() SKIPPED

... output for non-skippified tests ...

```

Skippy detects that both skippified tests can be skipped:

- There was no source or bytecode change in any of the skippified tests (compared to the data in the `skippy` folder).
- There was no source or bytecode change in any of the covered classes (compared to the data in the `skippy` folder).

## Testing After Modifications

When changes are made, Skippy reassesses which tests to run.

Modify `StringUtils`:

```
class StringUtils {
    
    String uselessProperty = "useless"; // artificial code change
    
    ...
}
```

Re-run the tests:
```
./gradlew test
```

Observe that Skippy runs the skippified tests again:
```
LeftPadderTest
DEBUG i.s.c.m.SkippyAnalysisResult - com.example.LeftPadderTest: Source change in covered class 'com.example.StringUtils' detected. Execution required.
LeftPadderTest > testPadLeft() PASSED

RightPadderTest
DEBUG i.s.c.m.SkippyAnalysisResult - com.example.RightPadderTest: Source change in covered class 'com.example.StringUtils' detected. Execution required.
RightPadderTest > testPadLeft() PASSED

... output for non-skippified tests ...
```

Undo your changes in preparation for the next iteration:
```
git stash
```

Now, let's see what happens if you change `LeftPadderTest`:
```
@ExtendWith(Skippy.class)
public class LeftPadderTest {

    String uselessProperty = "useless"; // artificial code change

    ...

}
```

Re-run the tests:
```
./gradlew test
```

Observe that Skippy only re-runs `LeftPadderTest`:
```
LeftPadderTest
DEBUG i.s.c.m.SkippyAnalysisResult - com.example.LeftPadderTest: Source change detected. Execution required.
LeftPadderTest > testPadLeft() PASSED

RightPadderTest
DEBUG i.s.c.m.SkippyAnalysisResult - com.example.RightPadderTest: No changes in test or covered classes detected. Execution skipped.
RightPadderTest > testPadLeft() SKIPPED

... output for non-skippified tests ...
```

Undo your changes in preparation for the next iteration:
```
git stash
```

Lastly, let's see what happens if you change the value of the constant in `TestConstants`:

```
class TestConstants {

    // static final String HELLO = "hello";
    static final String HELLO = "bonjour";

}
```

Re-run the tests:

```
./gradlew test
```

Skippy detected the bytecode changes and re-runs both skippified tests:
```
LeftPadderTest
DEBUG i.s.c.m.SkippyAnalysisResult - com.example.LeftPadderTest: Bytecode change detected. Execution required.
LeftPadderTest > testPadLeft() FAILED

RightPadderTest
DEBUG i.s.c.m.SkippyAnalysisResult - com.example.RightPadderTest: Bytecode change detected. Execution required.
RightPadderTest > testPadLeft() FAILED

... output for non-skippified tests ...
```

Congratulations! You've successfully integrated Skippy into your project, ensuring that only necessary tests are run, 
saving you time and resources.