# Skippy

Skippy is designed for the modern pace of software development, where Continuous Integration (CI) should be a boon, not 
a burden. Unlike traditional CI tools that run all tests regardless of necessity, Skippy's intelligent test impact
analysis detects and skips tests unaffected by recent changes. This smart approach cuts down on unnecessary testing 
and flakiness, slashing build times to improve developer productivity and happiness.

It’s about working smarter, not harder—let Skippy streamline your builds, so you can get back to what CI was meant to 
be: a fast track to production. Built upon robust frameworks like [JaCoCo](https://github.com/jacoco/jacoco) and [ASM](https://asm.ow2.io/), Skippy lets you deliver quality code 
faster without compromising the integrity of your builds. 

## Highlights

- Open Source under Apache License Version 2.0
- Skippy has all your workflows covered: Whether you execute tests from the command-line, your favorite IDE or your CI pipeline.
- You are in full control: Whether to roll it out for a single test or an entire suite.
- Coming soon: Accurate test coverage metrics even if tests are being skipped.

## How Skippy Works

Skippy improves test efficiency with a two-pronged approach: 
- [a powerful build plugin](skippy-gradle) and 
- [a smart JUnit extension](skippy-junit5). 

The build plugin harnesses the power of JaCoCo's coverage analysis and ASM's bytecode instrumentation to capture 
detailed execution data for each test. In addition, the plugin captures a hash of each source file. Then, Skippy's JUnit
extension takes over, analyzing project changes to identify the tests that are affected by recent updates.

The result? A streamlined test suite that only runs what's needed.

## Further Reading

- [Getting Started with Skippy, Gradle & JUnit 5](SKIPPY-GRADLE-JUNIT5.md)
- [Roadmap](ROADMAP.md)