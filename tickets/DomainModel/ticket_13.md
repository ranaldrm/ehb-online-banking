# EHB Ticket 013 — Add JUnit 5 to the Maven Project

## Objective

Introduce automated testing infrastructure to the project.

At this stage, the application is **plain Java 21**. Do not use Spring Boot, a database, REST APIs, or any external banking infrastructure.

The purpose of this ticket is to practise Maven dependency management, test scope configuration, and the JUnit 5 project structure.

## Requirements

* Add JUnit 5 (JUnit Jupiter) to `pom.xml` with `test` scope.
* Configure the Maven Surefire plugin if necessary to ensure tests are discovered and run.
* Verify that tests can be executed with:

```text
mvn test
```

* Create the appropriate test package structure under:

```text
src/test/java/
```

The test package should mirror the main source package:

```java
package com.ehb.banking;
```

So the test directory structure should be:

```text
src/test/java/com/ehb/banking/
```

## Adding the Dependency

Add JUnit Jupiter to `pom.xml`:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.2</version>
    <scope>test</scope>
</dependency>
```

Ensure the Maven Surefire plugin version supports JUnit 5 test discovery. A suitable version is `3.x` or later:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.5</version>
</plugin>
```

## Smoke Test

Create a minimal test class to confirm the setup is working:

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmokeTest {
    @Test
    void junitIsWorking() {
        assertTrue(true);
    }
}
```

Run:

```text
mvn test
```

and confirm the test is discovered and passes.

## Package

Tests live in the mirrored test source tree:

```text
src/test/java/com/ehb/banking/
```

## Files

For this ticket, modify:

```text
pom.xml
```

And create:

```text
src/test/java/com/ehb/banking/SmokeTest.java
```

(or a similarly named placeholder test class.)

## Things to Think About

While completing this ticket, consider:

* What is `test` scope, and why is it appropriate for JUnit?
* Why does the test directory structure mirror the main source directory?
* What is the Maven Surefire plugin and why is its version relevant for JUnit 5?
* What happens if you run `mvn package` — do tests run as part of that lifecycle?

## Out of Scope

Do **not** write actual domain tests yet — those are covered in Tickets 014, 015, and 016.

Do **not** implement:

* Spring Boot
* Spring Test
* Mockito (unless you choose to add it as a separate decision)
* JPA
* PostgreSQL
* Kafka
* AWS

## Definition of Done

The ticket is complete when:

* JUnit 5 is added to `pom.xml` with `test` scope.
* The Maven Surefire plugin is configured appropriately.
* The test source directory structure exists under `src/test/java/com/ehb/banking/`.
* A smoke test exists and passes.
* Running `mvn test` completes successfully with the smoke test passing.
* You understand the Maven test configuration and can explain it.
