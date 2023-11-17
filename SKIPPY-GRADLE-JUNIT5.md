# Getting Started with Skippy, Gradle & JUnit 5

## Step 1: Apply the Skippy plugin

First, you have to apply the Skippy plugin:

```
buildscript {
    repositories {
        maven { url 'https://maven.pkg.github.com/skippy-io/skippy' }
    }
    dependencies {
        classpath 'io.skippy:skippy-gradle:0.0.1-SNAPSHOT'
    }
}

apply plugin: io.skippy.gradle.SkippyPlugin
```

The Skippy plugin adds a couple of tasks to your project:

- `skippyAnalysis` performs a test impact analysis and captures a hash for each source file.
- `skippyClean` removes data captured by `skippyAnalysis`.

## Step 2: Add the skippy-junit5 dependency

Next, you have to add a dependency to `skippy-junit5`:

```
repositories {
    maven("https://maven.pkg.github.com/skippy-io/skippy")
}

dependencies {
    ...
    testImplementation 'io.skippy:skippy-junit5:0.0.1-SNAPSHOT'
}
```

## Step 3: Integrate Skippy with Your Tests

Annotate the tests you want to 'skippify' with the Skippy extension:
```
import io.skippy.Skippy;

@ExtendWith(Skippy.class)
public class FooTest {

    @Test
    void testFoo() {
        assertEquals("hello", Foo.hello());
    }
}
```

## Step 4: Run Your Conditional Tests

Execute your tests:
```
./gradlew test
```

You should see something like this (assuming you did not run `skippyAnalysis` before):
```
FooTest > testFoo() PASSED
```
Skippy has no historic execution data to decide whether `FooTest` needs to run. In this case,
Skippy will always execute a test.

Let's change that by executing `skippyAnalysis`:
```
./gradlew skippyAnalysis
```

Now, let's execute the test again:
```
./gradlew test                 
```

You should see something like this: 
```
FooTest > testFoo() SKIPPED
```

Skippy detects that the test can be skipped based on the test impact analysis performed by `skippyAnalysis`. 

## Step 5: Test After Making Changes

Make changes to the `Foo` class, then rerun your tests:

```
./gradlew test
```

Observe that Skippy runs the test again:
```
FooTest > testFoo() PASSED
```

Congratulations! You've successfully integrated Skippy into your project, ensuring that only necessary tests are run, saving you time and resources.

## Roadmap

Skippy just got started: The current focus is to apply it to large real-life projects to iron out any teething 
problems. The next items on the roadmap are

- proper logging,
- support for JUnit 4,
- support for incremental analysis updates,
- support for reasoning based on changes in the classpath (e.g., a version bump in `build.gradle`)
- support for reasoning based on updated resources (e.g, an updated setting in `src/main/resource/application.properties`),
- distribution via Maven Central,
- distribution via Gradle Plugin Portal,
- skipped tests contributing to JaCoCo reports and execution data files,
- support for Maven-based builds and
- support for other JVM-based languages.

