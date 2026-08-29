# EHB Online Banking --- Stage 2 Guided Implementation Tickets

## Stage 2: Spring Boot + REST

This document replaces the earlier high-level Stage 2 ticket list with a
**guided implementation sequence**.

Each ticket is designed to be completed one at a time. The tickets
deliberately provide more guidance than a normal software-development
backlog because Stage 2 introduces Spring Boot and REST for the first
time in this project.

The aim is still for **you to write the important implementation code
yourself**. The instructions tell you where to begin, what concepts you
are encountering, which files are likely to change, how to check your
work, and what you should understand before moving on.

Stage 2 remains deliberately **in memory**. Do not introduce JPA,
Hibernate, PostgreSQL, Kafka, Docker, AWS, microservices, or real
authentication during this stage.

The target architecture is:

``` text
HTTP client
    ↓
embedded web server
    ↓
Spring MVC
    ↓
Controller
    ↓
Service
    ↓
Domain model
    ↓
DTO
    ↓
Jackson
    ↓
JSON
```

------------------------------------------------------------------------

# Ticket S2-01 --- Add Spring Boot to the Existing Maven Project

## Objective

Convert the existing EHB Maven project from a plain Java 21 project into
a project with Spring Boot dependencies.

Do **not** create the Spring Boot application class yet.

The purpose of this ticket is to understand what Spring Boot adds to the
Maven build without changing the existing domain model.

## Current Project State

You already have:

-   Java 21;
-   Maven;
-   JUnit 5;
-   the completed Stage 1 domain model;
-   existing domain tests.

The project currently has no Spring dependencies.

## Files You Will Work With

``` text
pom.xml
```

## New Concepts

### Spring

Spring is a Java framework that provides facilities including dependency
injection, web application support, persistence integration, and testing
support.

### Spring Boot

Spring Boot sits on top of Spring and makes Spring applications easier
to configure and run by providing conventions, dependency management,
starters, and auto-configuration.

### Maven Dependency

A Maven dependency tells Maven that the project requires an external
library.

You have already encountered this idea with JUnit.

### Spring Boot Starter

A starter groups dependencies commonly needed for a particular kind of
application.

For this project you will introduce the web starter:

``` text
spring-boot-starter-web
```

## Implementation Steps

1.  Open the existing `pom.xml`.
2.  Identify the current `groupId`, `artifactId`, Java version, and
    JUnit configuration.
3.  Add Spring Boot dependency management in the normal Maven/Spring
    Boot manner.
4.  Add the Spring Boot web starter.
5.  Do not add unrelated starters.
6.  Run the existing test suite.
7.  Build the project.

## Commands to Use

``` bash
mvn test
```

and:

``` bash
mvn clean package
```

## Expected Result

The existing Stage 1 tests still pass and Maven can build the project
with Spring Boot dependencies available.

There is no requirement for a web server to start yet.

## Things to Think About

-   What is the difference between Spring and Spring Boot?
-   What does Maven do when you add a dependency?
-   Why does a starter exist?
-   Why use the web starter instead of manually selecting every Spring
    web library?
-   Why are you modifying the existing EHB project instead of generating
    a new unrelated project?

## Out of Scope

Do not implement:

-   `@SpringBootApplication`;
-   `SpringApplication.run(...)`;
-   controllers;
-   services;
-   REST endpoints;
-   JPA;
-   PostgreSQL;
-   Spring Security.

## Definition of Done

-   Spring Boot dependency management is configured.
-   The web starter is present.
-   Existing tests pass.
-   `mvn clean package` succeeds.
-   You can explain Spring vs Spring Boot and what a starter is.

------------------------------------------------------------------------

# Ticket S2-02 --- Create the Spring Boot Application Entry Point

## Objective

Create the class that starts EHB as a Spring Boot application.

The purpose is to understand how Spring Boot starts, creates its
application context, and launches an embedded web server.

## Files You Will Work With

Create an application class under the root EHB package, for example:

``` text
src/main/java/com/ehb/banking/EhbBankingApplication.java
```

The exact class name can vary, but it should live high enough in the
package hierarchy for Spring component scanning to discover later
application components.

## New Concepts

### `@SpringBootApplication`

This annotation marks the main Spring Boot application configuration
class.

At a high level it enables:

-   configuration;
-   auto-configuration;
-   component scanning.

You do not need to understand its internal implementation yet.

### `SpringApplication.run(...)`

This starts the Spring application.

At a high level Spring Boot:

1.  creates the application context;
2.  examines configuration and dependencies;
3.  discovers Spring-managed components;
4.  performs auto-configuration;
5.  starts the embedded web server.

### Application Context

The application context is Spring's container for objects that Spring
creates and manages.

Those managed objects are called **beans**.

## Implementation Steps

1.  Create the application class.
2.  Give it a normal Java `main` method.
3.  Mark the class as the Spring Boot application.
4.  Start Spring from `main`.
5.  Run the application.
6.  Read the console output rather than immediately closing it.
7.  Identify evidence that the embedded server has started.

## Commands to Try

Depending on your Maven configuration, run the application using the
Spring Boot Maven command or directly from your IDE.

Also verify:

``` bash
mvn test
```

still succeeds.

## Expected Resultmvn spring-boot:run
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------------< com.ehb:ehb-online-banking >---------------------
[INFO] Building ehb-online-banking 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] >>> spring-boot-maven-plugin:3.2.5:run (default-cli) > test-compile @ ehb-online-banking >>>
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ ehb-online-banking ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /home/ranaldrm/Projects/ehb-online-banking/src/main/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.13.0:compile (default-compile) @ ehb-online-banking ---
[INFO] Nothing to compile - all classes are up to date.
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ ehb-online-banking ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /home/ranaldrm/Projects/ehb-online-banking/src/test/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.13.0:testCompile (default-testCompile) @ ehb-online-banking ---
[INFO] Nothing to compile - all classes are up to date.
[INFO] 
[INFO] <<< spring-boot-maven-plugin:3.2.5:run (default-cli) < test-compile @ ehb-online-banking <<<
[INFO] 
[INFO] 
[INFO] --- spring-boot-maven-plugin:3.2.5:run (default-cli) @ ehb-online-banking ---
Downloading from central: https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-buildpack-platform/3.2.5/spring-boot-buildpack-platform-3.2.5.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-buildpack-platform/3.2.5/spring-boot-buildpack-platform-3.2.5.pom (3.2 kB at 7.3 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.14.2/jackson-databind-2.14.2.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.14.2/jackson-databind-2.14.2.pom (19 kB at 241 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-base/2.14.2/jackson-base-2.14.2.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-base/2.14.2/jackson-base-2.14.2.pom (10 kB at 114 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-bom/2.14.2/jackson-bom-2.14.2.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-bom/2.14.2/jackson-bom-2.14.2.pom (17 kB at 206 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-parent/2.14/jackson-parent-2.14.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/jackson-parent/2.14/jackson-parent-2.14.pom (7.7 kB at 98 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/oss-parent/48/oss-parent-48.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/oss-parent/48/oss-parent-48.pom (24 kB at 279 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.14.2/jackson-annotations-2.14.2.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.14.2/jackson-annotations-2.14.2.pom (6.2 kB at 82 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-core/2.14.2/jackson-core-2.14.2.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-core/2.14.2/jackson-core-2.14.2.pom (7.0 kB at 88 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/module/jackson-module-parameter-names/2.14.2/jackson-module-parameter-names-2.14.2.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/module/jackson-module-parameter-names/2.14.2/jackson-module-parameter-names-2.14.2.pom (4.4 kB at 53 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/module/jackson-modules-java8/2.14.2/jackson-modules-java8-2.14.2.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/module/jackson-modules-java8/2.14.2/jackson-modules-java8-2.14.2.pom (3.1 kB at 39 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/net/java/dev/jna/jna-platform/5.13.0/jna-platform-5.13.0.pom
Downloaded from central: https://repo.maven.apache.org/maven2/net/java/dev/jna/jna-platform/5.13.0/jna-platform-5.13.0.pom (2.3 kB at 26 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/net/java/dev/jna/jna/5.13.0/jna-5.13.0.pom
Downloaded from central: https://repo.maven.apache.org/maven2/net/java/dev/jna/jna/5.13.0/jna-5.13.0.pom (2.0 kB at 25 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-compress/1.21/commons-compress-1.21.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-compress/1.21/commons-compress-1.21.pom (20 kB at 237 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/client5/httpclient5/5.2.3/httpclient5-5.2.3.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/client5/httpclient5/5.2.3/httpclient5-5.2.3.pom (6.0 kB at 74 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/client5/httpclient5-parent/5.2.3/httpclient5-parent-5.2.3.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/client5/httpclient5-parent/5.2.3/httpclient5-parent-5.2.3.pom (17 kB at 186 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpcomponents-parent/13/httpcomponents-parent-13.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpcomponents-parent/13/httpcomponents-parent-13.pom (30 kB at 325 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/apache/27/apache-27.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/apache/27/apache-27.pom (20 kB at 245 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/core5/httpcore5/5.2.4/httpcore5-5.2.4.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/core5/httpcore5/5.2.4/httpcore5-5.2.4.pom (3.9 kB at 46 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/core5/httpcore5-parent/5.2.4/httpcore5-parent-5.2.4.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/core5/httpcore5-parent/5.2.4/httpcore5-parent-5.2.4.pom (14 kB at 178 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/core5/httpcore5-h2/5.2.4/httpcore5-h2-5.2.4.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/core5/httpcore5-h2/5.2.4/httpcore5-h2-5.2.4.pom (3.6 kB at 48 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/springframework/spring-core/6.0.10/spring-core-6.0.10.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/springframework/spring-core/6.0.10/spring-core-6.0.10.pom (2.0 kB at 25 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/springframework/spring-jcl/6.0.10/spring-jcl-6.0.10.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/springframework/spring-jcl/6.0.10/spring-jcl-6.0.10.pom (1.8 kB at 22 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/tomlj/tomlj/1.0.0/tomlj-1.0.0.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/tomlj/tomlj/1.0.0/tomlj-1.0.0.pom (2.8 kB at 35 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/antlr/antlr4-runtime/4.7.2/antlr4-runtime-4.7.2.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/antlr/antlr4-runtime/4.7.2/antlr4-runtime-4.7.2.pom (3.6 kB at 43 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/antlr/antlr4-master/4.7.2/antlr4-master-4.7.2.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/antlr/antlr4-master/4.7.2/antlr4-master-4.7.2.pom (4.4 kB at 56 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.pom
Downloaded from central: https://repo.maven.apache.org/maven2/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.pom (4.3 kB at 50 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-loader-tools/3.2.5/spring-boot-loader-tools-3.2.5.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-loader-tools/3.2.5/spring-boot-loader-tools-3.2.5.pom (2.2 kB at 24 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/shared/maven-common-artifact-filters/3.3.2/maven-common-artifact-filters-3.3.2.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/shared/maven-common-artifact-filters/3.3.2/maven-common-artifact-filters-3.3.2.pom (5.3 kB at 61 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/shared/maven-shared-components/37/maven-shared-components-37.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/shared/maven-shared-components/37/maven-shared-components-37.pom (4.9 kB at 57 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/maven-parent/37/maven-parent-37.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/maven-parent/37/maven-parent-37.pom (46 kB at 563 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-shade-plugin/3.5.0/maven-shade-plugin-3.5.0.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-shade-plugin/3.5.0/maven-shade-plugin-3.5.0.pom (12 kB at 159 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-plugins/39/maven-plugins-39.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-plugins/39/maven-plugins-39.pom (8.1 kB at 96 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/3.5.1/plexus-utils-3.5.1.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/3.5.1/plexus-utils-3.5.1.pom (8.8 kB at 112 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus/10/plexus-10.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus/10/plexus-10.pom (25 kB at 310 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/slf4j/slf4j-api/1.7.32/slf4j-api-1.7.32.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/slf4j/slf4j-api/1.7.32/slf4j-api-1.7.32.pom (3.8 kB at 49 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/slf4j/slf4j-parent/1.7.32/slf4j-parent-1.7.32.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/slf4j/slf4j-parent/1.7.32/slf4j-parent-1.7.32.pom (14 kB at 179 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/ow2/asm/asm/9.5/asm-9.5.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/ow2/asm/asm/9.5/asm-9.5.pom (2.4 kB at 30 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/ow2/asm/asm-commons/9.5/asm-commons-9.5.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/ow2/asm/asm-commons/9.5/asm-commons-9.5.pom (2.8 kB at 37 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/ow2/asm/asm-tree/9.5/asm-tree-9.5.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/ow2/asm/asm-tree/9.5/asm-tree-9.5.pom (2.6 kB at 32 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/jdom/jdom2/2.0.6.1/jdom2-2.0.6.1.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/jdom/jdom2/2.0.6.1/jdom2-2.0.6.1.pom (4.6 kB at 59 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/shared/maven-dependency-tree/3.2.1/maven-dependency-tree-3.2.1.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/shared/maven-dependency-tree/3.2.1/maven-dependency-tree-3.2.1.pom (6.2 kB at 85 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/commons-io/commons-io/2.13.0/commons-io-2.13.0.pom
Downloaded from central: https://repo.maven.apache.org/maven2/commons-io/commons-io/2.13.0/commons-io-2.13.0.pom (20 kB at 278 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-parent/58/commons-parent-58.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-parent/58/commons-parent-58.pom (83 kB at 1.0 MB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/vafer/jdependency/2.8.0/jdependency-2.8.0.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/vafer/jdependency/2.8.0/jdependency-2.8.0.pom (14 kB at 171 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-collections4/4.4/commons-collections4-4.4.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-collections4/4.4/commons-collections4-4.4.pom (24 kB at 294 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-parent/48/commons-parent-48.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-parent/48/commons-parent-48.pom (72 kB at 838 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-buildpack-platform/3.2.5/spring-boot-buildpack-platform-3.2.5.jar
Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.14.2/jackson-databind-2.14.2.jar
Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.14.2/jackson-annotations-2.14.2.jar
Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-core/2.14.2/jackson-core-2.14.2.jar
Downloading from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/module/jackson-module-parameter-names/2.14.2/jackson-module-parameter-names-2.14.2.jar
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.14.2/jackson-annotations-2.14.2.jar (77 kB at 568 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/net/java/dev/jna/jna-platform/5.13.0/jna-platform-5.13.0.jar
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/module/jackson-module-parameter-names/2.14.2/jackson-module-parameter-names-2.14.2.jar (9.5 kB at 66 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/net/java/dev/jna/jna/5.13.0/jna-5.13.0.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-buildpack-platform/3.2.5/spring-boot-buildpack-platform-3.2.5.jar (272 kB at 1.9 MB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-compress/1.21/commons-compress-1.21.jar
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-core/2.14.2/jackson-core-2.14.2.jar (459 kB at 1.7 MB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/client5/httpclient5/5.2.3/httpclient5-5.2.3.jar
Downloaded from central: https://repo.maven.apache.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.14.2/jackson-databind-2.14.2.jar (1.6 MB at 5.5 MB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/core5/httpcore5/5.2.4/httpcore5-5.2.4.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-compress/1.21/commons-compress-1.21.jar (1.0 MB at 3.2 MB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/core5/httpcore5-h2/5.2.4/httpcore5-h2-5.2.4.jar
Downloaded from central: https://repo.maven.apache.org/maven2/net/java/dev/jna/jna-platform/5.13.0/jna-platform-5.13.0.jar (1.4 MB at 4.2 MB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/tomlj/tomlj/1.0.0/tomlj-1.0.0.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/client5/httpclient5/5.2.3/httpclient5-5.2.3.jar (843 kB at 2.3 MB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/antlr/antlr4-runtime/4.7.2/antlr4-runtime-4.7.2.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/tomlj/tomlj/1.0.0/tomlj-1.0.0.jar (157 kB at 397 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar
Downloaded from central: https://repo.maven.apache.org/maven2/net/java/dev/jna/jna/5.13.0/jna-5.13.0.jar (1.9 MB at 4.5 MB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-loader-tools/3.2.5/spring-boot-loader-tools-3.2.5.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/core5/httpcore5/5.2.4/httpcore5-5.2.4.jar (855 kB at 2.0 MB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/shared/maven-common-artifact-filters/3.3.2/maven-common-artifact-filters-3.3.2.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/antlr/antlr4-runtime/4.7.2/antlr4-runtime-4.7.2.jar (338 kB at 748 kB/s)
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/httpcomponents/core5/httpcore5-h2/5.2.4/httpcore5-h2-5.2.4.jar (237 kB at 525 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/1.5.8/plexus-utils-1.5.8.jar
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-shade-plugin/3.5.0/maven-shade-plugin-3.5.0.jar
Downloaded from central: https://repo.maven.apache.org/maven2/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar (20 kB at 43 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/ow2/asm/asm/9.5/asm-9.5.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/shared/maven-common-artifact-filters/3.3.2/maven-common-artifact-filters-3.3.2.jar (58 kB at 114 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/ow2/asm/asm-commons/9.5/asm-commons-9.5.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-shade-plugin/3.5.0/maven-shade-plugin-3.5.0.jar (147 kB at 278 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/ow2/asm/asm-tree/9.5/asm-tree-9.5.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-loader-tools/3.2.5/spring-boot-loader-tools-3.2.5.jar (435 kB at 820 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/jdom/jdom2/2.0.6.1/jdom2-2.0.6.1.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/ow2/asm/asm/9.5/asm-9.5.jar (122 kB at 225 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/shared/maven-dependency-tree/3.2.1/maven-dependency-tree-3.2.1.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/1.5.8/plexus-utils-1.5.8.jar (268 kB at 458 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/commons-io/commons-io/2.13.0/commons-io-2.13.0.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/ow2/asm/asm-commons/9.5/asm-commons-9.5.jar (72 kB at 120 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/vafer/jdependency/2.8.0/jdependency-2.8.0.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/ow2/asm/asm-tree/9.5/asm-tree-9.5.jar (52 kB at 85 kB/s)
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/shared/maven-dependency-tree/3.2.1/maven-dependency-tree-3.2.1.jar (43 kB at 70 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-collections4/4.4/commons-collections4-4.4.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/jdom/jdom2/2.0.6.1/jdom2-2.0.6.1.jar (328 kB at 515 kB/s)
Downloaded from central: https://repo.maven.apache.org/maven2/org/vafer/jdependency/2.8.0/jdependency-2.8.0.jar (233 kB at 339 kB/s)
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/commons/commons-collections4/4.4/commons-collections4-4.4.jar (752 kB at 1.0 MB/s)
Downloaded from central: https://repo.maven.apache.org/maven2/commons-io/commons-io/2.13.0/commons-io-2.13.0.jar (484 kB at 641 kB/s)
[INFO] Attaching agents: []

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.5)

2026-08-29T14:00:06.260+01:00  INFO 175355 --- [           main] com.ehb.banking.EhbBankingApplication    : Starting EhbBankingApplication using Java 21.0.12 with PID 175355 (/home/ranaldrm/Projects/ehb-online-banking/target/classes started by ranaldrm in /home/ranaldrm/Projects/ehb-online-banking)
2026-08-29T14:00:06.262+01:00  INFO 175355 --- [           main] com.ehb.banking.EhbBankingApplication    : No active profile set, falling back to 1 default profile: "default"
2026-08-29T14:00:06.755+01:00  INFO 175355 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2026-08-29T14:00:06.764+01:00  INFO 175355 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2026-08-29T14:00:06.764+01:00  INFO 175355 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.20]
2026-08-29T14:00:06.796+01:00  INFO 175355 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2026-08-29T14:00:06.796+01:00  INFO 175355 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 506 ms
2026-08-29T14:00:06.993+01:00  INFO 175355 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
2026-08-29T14:00:06.998+01:00  INFO 175355 --- [           main] com.ehb.banking.EhbBankingApplication    : Started EhbBankingApplication in 0.996 seconds (process running for 1.172)


The application starts and remains running because an embedded web
server is listening for HTTP requests.

You do not yet need a working endpoint.

## Things to Think About

-   What is a Spring bean?
-   What is the application context?
-   Who creates the application context?
-   Why does the application keep running after `main` starts Spring?
-   What is an embedded server?
-   Why should the application class sit near the root of the package
    structure?
-   How is this different from the old `BankingSystem.main()`
    scratchpad?

## Out of Scope

Do not create controllers or business functionality yet.

## Definition of Done

-   EHB starts as a Spring Boot application.
-   The embedded server starts without errors.
-   Existing domain tests pass.
-   You can explain the application context and bean concepts at a basic
    level.

------------------------------------------------------------------------

# Ticket S2-03 --- Create the First REST Endpoint

## Objective

Create the smallest possible HTTP endpoint.

The endpoint should simply confirm that the EHB application is running.
It must not use banking domain objects yet.

Suggested endpoint:

``` text
GET /api/status
```

## Files You Will Work With

Create a controller package if it does not exist:

``` text
src/main/java/com/ehb/banking/controller/
```

and a small controller class inside it.

## New Concepts

### Controller

A controller receives HTTP requests and decides which application
behaviour should handle them.

### `@RestController`

This tells Spring that the class contains web request handlers whose
return values should normally be written into HTTP response bodies.

### `@GetMapping`

This maps an HTTP GET request to a Java method.

### HTTP GET

GET is normally used to retrieve information without changing
application state.

## Implementation Steps

1.  Create the controller class.
2.  Mark it as a REST controller.
3.  Add a method mapped to `GET /api/status`.
4.  Initially return something extremely simple, such as text.
5.  Start the application.
6.  Request the endpoint using a browser, `curl`, or an API client.

## Example Request

``` text
GET http://localhost:8080/api/status
```

The port may differ if you have changed Spring's defaults.

## Expected Result

The request receives:

``` text
200 OK
```

and your simple response.

## Things to Think About

-   How did Spring discover the controller?
-   Who created the controller object?
-   Why did this Java method run for this URL?
-   What does `@GetMapping` describe?
-   What is the difference between starting the application and making
    an HTTP request to it?

## Out of Scope

Do not use:

-   `Account`;
-   `Business`;
-   services;
-   DTOs;
-   payment logic.

## Definition of Done

-   `/api/status` returns successfully.
-   You can make the request yourself.
-   You can explain how Spring maps the request to the controller
    method.

------------------------------------------------------------------------

# Ticket S2-04 --- Create Simple In-Memory Account Storage

## Objective

Give the running application a small collection of EHB accounts that can
be used by later REST endpoints.

This is temporary in-memory state for Stage 2, not database persistence.

## Files You Will Work With

Create a small class responsible for holding accounts. A suitable
package could be:

``` text
src/main/java/com/ehb/banking/repository/
```

or another clearly named in-memory storage package.

Do not confuse this with a JPA repository. There is no database yet.

## New Concepts

### Application State

The application now needs objects that remain available across multiple
HTTP requests while the program is running.

### Bean Lifecycle

A Spring-managed singleton bean normally exists for the lifetime of the
application context.

That makes a simple Spring-managed in-memory store useful for this
learning stage.

## Requirements

The store should:

-   contain multiple `Account` objects;
-   allow an account to be found by account number;
-   use the existing Stage 1 `Account` class;
-   remain deliberately simple.

You may initialise a few fictional accounts when the store is created.

## Implementation Steps

1.  Decide what Java collection is suitable for finding accounts by
    account number.
2.  Create the storage class.
3.  Make Spring manage one instance of it.
4.  Initialise at least two accounts.
5.  Give the class a way to retrieve an account by account number.
6.  Test the behaviour directly or temporarily inspect it while running.

## Expected Behaviour

While the application is running:

``` text
request 1 → account exists
request 2 → same application state still exists
```

After restarting:

``` text
application restarts → initial state recreated
```

## Things to Think About

-   Would a `List` or `Map` be more natural for lookup by account
    number?
-   Why is this not real persistence?
-   Why does restarting the application lose changes?
-   Why are we deliberately avoiding PostgreSQL at this point?
-   Does the storage class contain banking rules, or merely hold
    objects?

## Out of Scope

Do not introduce:

-   JPA;
-   `@Entity`;
-   Spring Data;
-   PostgreSQL;
-   SQL.

## Definition of Done

-   At least two accounts exist in memory.
-   They can be retrieved by account number.
-   The same store is available throughout the running application.
-   You understand why the data resets after restart.

------------------------------------------------------------------------

# Ticket S2-05 --- Introduce `AccountService` and Dependency Injection

## Objective

Create an `AccountService` between the web/controller layer and account
storage.

This is the first ticket where dependency injection becomes central.

## Target Flow

``` text
Controller
    ↓
AccountService
    ↓
In-memory account storage
```

## Files You Will Work With

Create:

``` text
src/main/java/com/ehb/banking/service/AccountService.java
```

You will also work with the in-memory store from S2-04.

## New Concepts

### Service Layer

A service represents application operations/use cases.

It coordinates domain objects and storage without being concerned with
HTML or HTTP details.

### `@Service`

This marks a class as a Spring service component so that Spring
discovers and manages it.

### Dependency Injection

If `AccountService` requires the account store, the service should
receive that dependency rather than constructing it itself.

Conceptually:

``` text
Without DI:

AccountService
    ↓
new AccountStore()


With Spring DI:

Spring creates AccountStore
Spring creates AccountService
Spring passes AccountStore to AccountService
```

### Constructor Injection

Dependencies should normally be supplied through the constructor.

## Requirements

`AccountService` should initially support one operation:

``` text
find/retrieve an account by account number
```

Do not add unrelated functionality.

## Implementation Steps

1.  Create `AccountService`.
2.  Mark it as a Spring service.
3.  Give its constructor the dependency it requires.
4.  Store that dependency in a field.
5.  Add one method for retrieving an account.
6.  Let Spring create the service rather than manually using
    `new AccountService(...)`.

## Things to Think About

-   Who creates `AccountService`?
-   Who creates the account store?
-   How does Spring know the service requires the store?
-   Why is constructor injection preferable to the service constructing
    its own dependency?
-   Is `AccountService` itself part of the banking domain?
-   What belongs in a service versus `Account`?

## Out of Scope

Do not add payment functionality or database repositories.

## Definition of Done

-   `AccountService` is Spring-managed.
-   Its storage dependency is constructor-injected.
-   It can retrieve an account.
-   You can explain dependency injection and inversion of control in
    your own words.

------------------------------------------------------------------------

# Ticket S2-06 --- Retrieve an Account Through REST

## Objective

Connect the pieces built so far into the first real EHB endpoint.

Create:

``` text
GET /api/accounts/{accountNumber}
```

## Target Flow

``` text
HTTP GET
    ↓
AccountController
    ↓
AccountService
    ↓
in-memory store
    ↓
Account
```

## Files You Will Work With

You will probably modify/create:

``` text
controller/AccountController.java
service/AccountService.java
```

## New Concepts

### `@PathVariable`

A path variable extracts a value from the URL.

For example:

``` text
/api/accounts/ACC001
              └────┘
            accountNumber
```

## Requirements

The controller should:

1.  receive the account number from the URL;
2.  call `AccountService`;
3.  return the retrieved account.

For this ticket only, returning the domain `Account` directly is
acceptable. The next ticket will correct that design.

## Implementation Steps

1.  Create or extend `AccountController`.
2.  Inject `AccountService` through the controller constructor.
3.  Map a GET request containing an account number.
4.  Extract the account number using a path variable.
5.  call the service.
6.  run the application;
7.  request a known account.

## Things to Think About

-   Who creates `AccountController`?
-   Why does the controller not construct `AccountService`?
-   Why does the controller not directly inspect the storage collection?
-   Which layer knows about HTTP?
-   Which layer knows how to retrieve accounts?

## Definition of Done

-   A known account can be retrieved through HTTP.
-   The request passes controller → service → storage.
-   You can explain every step in that flow.

------------------------------------------------------------------------

# Ticket S2-07 --- Introduce an Account Response DTO

## Objective

Stop exposing the internal `Account` object directly through the API.

Create a deliberately designed response DTO.

## Target Flow

``` text
Account
   ↓
AccountResponse
   ↓
Jackson
   ↓
JSON
```

## Files You Will Work With

Create a DTO package:

``` text
src/main/java/com/ehb/banking/dto/
```

and an account response type, for example:

``` text
AccountResponse.java
```

## New Concepts

### DTO

DTO means **Data Transfer Object**.

A DTO represents data crossing an application boundary.

The domain object answers:

> How should an account behave internally?

The response DTO answers:

> What account information should this API expose?

### Java Records

A Java record is a good fit for many immutable DTOs because its purpose
is primarily to carry data.

### Jackson

Spring Boot's web support uses Jackson to convert Java objects into JSON
and JSON into Java objects.

## Requirements

Create a response containing only information that the API deliberately
exposes, such as:

-   account number;
-   currency;
-   balance.

Map an `Account` to `AccountResponse`.

Return the DTO from the controller.

## Implementation Steps

1.  Create the DTO.
2.  Decide which account fields belong in the public response.
3.  Create the mapping from domain object to DTO.
4.  Change the controller to return the DTO.
5.  call the endpoint again;
6.  inspect the JSON.

## Things to Think About

-   Why is `AccountResponse` different from `Account`?
-   What would happen if the internal `Account` class changed?
-   Why is controlling exposed fields useful?
-   Who turns the DTO into JSON?
-   Why are records convenient here?

## Definition of Done

-   The endpoint returns `AccountResponse`, not `Account`.
-   JSON contains only intended fields.
-   Domain classes do not need web-specific changes.
-   You can explain DTOs and Jackson at a basic level.

------------------------------------------------------------------------

# Ticket S2-08 --- Handle Unknown Accounts with HTTP 404

## Objective

Make the API respond sensibly when an account cannot be found.

Instead of exposing an uncontrolled Java exception or generic 500 error,
return:

``` text
404 Not Found
```

## Files You May Work With

``` text
service/AccountService.java
exception/AccountNotFoundException.java
```

and a new API exception-handling class.

## New Concepts

### HTTP 404

404 means that the requested resource could not be found.

### `@RestControllerAdvice`

This allows exception-handling behaviour to be applied across REST
controllers.

### `@ExceptionHandler`

This associates a particular exception type with code that builds an
HTTP response.

## Requirements

When an unknown account is requested:

``` text
GET /api/accounts/UNKNOWN
```

the API should return 404.

Create a small structured error response rather than relying on a stack
trace.

## Implementation Steps

1.  Confirm what currently happens for an unknown account.
2.  Ensure the service throws the existing account-not-found exception
    where appropriate.
3.  Create a global REST exception handler.
4.  Handle `AccountNotFoundException`.
5.  Return an appropriate status and small error representation.
6.  test known and unknown accounts.

## Things to Think About

-   Why should the domain exception not itself need to understand HTTP?
-   Why is 404 more appropriate than 500?
-   Why centralise error translation instead of putting `try/catch` in
    every controller method?

## Definition of Done

-   Existing account → 200.
-   Unknown account → 404.
-   The error response is understandable.
-   No controller contains repetitive exception-handling logic.

------------------------------------------------------------------------

# Ticket S2-09 --- Expose Account Transaction History

## Objective

Expose the transaction history already recorded by the Stage 1 `Account`
domain model.

Create:

``` text
GET /api/accounts/{accountNumber}/transactions
```

## Files You Will Work With

Likely:

``` text
controller/AccountController.java
service/AccountService.java
dto/TransactionResponse.java
```

## New Concepts

-   nested resources;
-   mapping collections;
-   serialising `LocalDateTime`;
-   mapping domain records to API DTOs.

## Requirements

Create a `TransactionResponse` DTO containing appropriate transaction
information.

The endpoint should return a collection of transaction responses for the
requested account.

## Implementation Steps

1.  Inspect the existing `Transaction` record.
2.  Decide what belongs in `TransactionResponse`.
3.  Add a service operation to obtain transaction history if necessary.
4.  Add the nested GET endpoint.
5.  Map each transaction to the response DTO.
6.  Make requests before and after an operation that creates
    transactions.

## Things to Think About

-   Why use `/accounts/{accountNumber}/transactions`?
-   Why not expose the internal transaction collection directly?
-   How do Java streams or ordinary loops help map collections?
-   What JSON representation does the timestamp receive?

## Definition of Done

-   Known account transaction history is returned as JSON.
-   Unknown account still produces 404.
-   API uses transaction DTOs.
-   Existing domain behaviour is unchanged.

------------------------------------------------------------------------

# Ticket S2-10 --- Expose Businesses and Their Accounts

## Objective

Add a small read-only REST interface for businesses and the accounts
belonging to them.

## Suggested Endpoints

``` text
GET /api/businesses/{businessId}
GET /api/businesses/{businessId}/accounts
```

## Files You May Create

``` text
service/BusinessService.java
controller/BusinessController.java
dto/BusinessResponse.java
```

You may also need simple in-memory business storage or to adapt the
existing Stage 2 storage arrangement.

## Requirements

The API should be able to:

-   retrieve a business;
-   retrieve the accounts belonging to a business.

## Critical Constraint

The Stage 1 `Business` class contains a plaintext password field
intended for possible future login work.

**Do not expose that field through the API.**

This ticket is one reason DTOs matter.

## Implementation Steps

1.  Review the existing `Business` class.
2.  Decide which business fields are safe/useful to expose.
3.  Create a response DTO.
4.  create a business service;
5.  create a business controller;
6.  expose the two read endpoints;
7.  verify the password never appears in returned JSON.

## Things to Think About

-   What relationship exists between `Business` and `Account`?
-   Should a controller navigate the domain object graph itself or ask a
    service?
-   How does a DTO protect internal information?
-   What should happen for an unknown business?

## Definition of Done

-   A business can be retrieved.
-   Its accounts can be retrieved.
-   Password data is never returned.
-   Missing businesses produce a sensible API error.

------------------------------------------------------------------------

# Ticket S2-11 --- Create the Payment Request DTO

## Objective

Design the JSON contract that a client will use to request an outgoing
payment.

Do not create the POST endpoint yet.

## Files You Will Work With

Create something such as:

``` text
dto/PaymentRequest.java
```

## Example API Shape

Conceptually, a client should eventually send:

``` json
{
  "sourceAccountNumber": "ACC001",
  "targetAccountNumber": "ACC002",
  "amount": 100.00
}
```

## New Concepts

### Request DTO

A request DTO represents incoming API data.

It is not necessarily the same thing as the domain object that will
eventually be created.

### JSON Deserialisation

Jackson can convert incoming JSON into a Java object.

## Requirements

Create an immutable request DTO containing:

-   source account number;
-   target account number;
-   amount.

A Java record is a natural option.

## Implementation Steps

1.  Decide the Java types for the three values.
2.  Create the request DTO.
3.  Compare it with the existing `Payment` domain class.
4.  Explain why the API should not simply ask clients to construct a
    `Payment`.

## Things to Think About

-   Who should generate a payment ID?
-   Who should determine the timestamp?
-   Who should determine payment status?
-   Why should the client not control those values?
-   Why is `BigDecimal` appropriate for the amount?

## Definition of Done

-   `PaymentRequest` exists.
-   It contains only client-supplied information.
-   You can explain why it is different from `Payment`.

------------------------------------------------------------------------

# Ticket S2-12 --- Validate Incoming Payment Requests

## Objective

Add validation rules to the API boundary so obviously malformed payment
requests are rejected before payment processing starts.

## Files You Will Work With

Primarily:

``` text
dto/PaymentRequest.java
```

You may also need the appropriate Spring Boot validation dependency if
it is not already present.

## New Concepts

### Jakarta Bean Validation

Bean Validation provides declarative constraints such as:

``` text
@NotNull
@NotBlank
@Positive
```

### `@Valid`

Spring can use `@Valid` on an incoming request to trigger validation
before the controller proceeds.

## Requirements

Validate at least:

-   source account number is present/non-blank;
-   target account number is present/non-blank;
-   amount is present;
-   amount is positive.

## Important Distinction

API validation asks:

``` text
Is this incoming request structurally acceptable?
```

Domain validation asks:

``` text
Is this banking operation actually legal?
```

Do not remove Stage 1 domain safeguards.

## Implementation Steps

1.  Add the validation dependency if required.
2.  Add suitable constraints to `PaymentRequest`.
3.  Learn what each chosen annotation checks.
4.  Do not yet duplicate sufficient-funds or payment-state rules in the
    DTO.

## Things to Think About

-   Why validate positive amount in both API and domain layers?
-   What happens if domain code is called from somewhere other than
    HTTP?
-   Is "source account has sufficient funds" a structural request rule
    or a banking rule?

## Definition of Done

-   Request DTO has sensible structural constraints.
-   Domain validation remains intact.
-   You can distinguish API validation from domain validation.

------------------------------------------------------------------------

# Ticket S2-13 --- Introduce `PaymentService`

## Objective

Create an application service that coordinates the existing Stage 1
payment behaviour.

## Target Flow

``` text
Payment request data
       ↓
PaymentService
       ├── find source account
       ├── find target account
       ↓
sourceAccount.processOutgoingPayment(...)
       ↓
Payment
```

## Files You Will Work With

Create:

``` text
service/PaymentService.java
```

and reuse `AccountService` or the relevant account-storage abstraction.

## New Concept: Orchestration

The service layer coordinates multiple objects needed to complete a use
case.

It should **not** reimplement rules already owned by `Account`,
`Payment`, or validators.

## Requirements

The service should accept the information required for a payment, find
the two accounts, and invoke the existing Stage 1 domain operation.

## Implementation Steps

1.  Review `Account.processOutgoingPayment(...)`.
2.  Identify what the service needs in order to call it.
3.  Create `PaymentService`.
4.  Inject the account-related dependency it needs.
5.  Retrieve source and target accounts.
6.  Delegate payment processing to the domain model.
7.  Return the resulting `Payment`.

## Things to Think About

-   Why does finding two accounts belong naturally in the service layer?
-   Why should sufficient-funds logic stay in the domain?
-   What happens if either account number is unknown?
-   Does `PaymentService` need to know anything about HTTP?

## Definition of Done

-   The service can orchestrate a valid payment.
-   It does not duplicate domain rules.
-   Dependencies are injected.
-   You can explain application orchestration.

------------------------------------------------------------------------

# Ticket S2-14 --- Create the Payment POST Endpoint

## Objective

Expose the existing payment workflow through HTTP.

Create:

``` text
POST /api/payments
```

## Target Flow

``` text
JSON
 ↓
PaymentRequest
 ↓
PaymentController
 ↓
PaymentService
 ↓
Account / Payment domain behaviour
```

## Files You Will Work With

Create or modify:

``` text
controller/PaymentController.java
dto/PaymentRequest.java
service/PaymentService.java
```

## New Concepts

### `@PostMapping`

Maps an HTTP POST request to a controller method.

### `@RequestBody`

Tells Spring to obtain a method argument from the HTTP request body.

Jackson can convert the JSON body into `PaymentRequest`.

### `@Valid`

Triggers the validation rules added in S2-12.

## Requirements

The controller should:

1.  receive a JSON request;
2.  convert it into `PaymentRequest`;
3.  validate it;
4.  call `PaymentService`;
5.  return the result.

For this ticket, a temporary response representation is acceptable.
S2-15 introduces the final payment response DTO.

## Things to Think About

-   What happens before your controller method runs?
-   Who converts JSON into `PaymentRequest`?
-   Where is request validation triggered?
-   Which layer actually performs banking rules?

## Definition of Done

-   A valid POST initiates a payment.
-   Account balances/transactions change according to existing domain
    behaviour.
-   Invalid structural requests are rejected.
-   Controller delegates to the service.

------------------------------------------------------------------------

# Ticket S2-15 --- Create the Payment Response DTO

## Objective

Stop returning the internal `Payment` object directly.

Create a deliberate API response representation.

## Files You Will Work With

Create:

``` text
dto/PaymentResponse.java
```

and modify the payment controller/mapping code.

## Requirements

Choose appropriate response fields. Likely candidates include:

-   payment ID;
-   source account number;
-   target account number;
-   amount;
-   timestamp;
-   payment status.

Do not expose implementation details merely because they exist on the
domain object.

## Implementation Steps

1.  Inspect `Payment`.
2.  Decide what a client needs to know after a payment.
3.  Create an immutable response DTO.
4.  map `Payment` to `PaymentResponse`;
5.  return the response from the POST endpoint;
6.  inspect the generated JSON.

## Things to Think About

-   Why have separate request and response DTOs?
-   Why does the client provide less information than the server
    returns?
-   How does Jackson serialise `PaymentStatus`?
-   Which HTTP status is appropriate for a successful payment operation?

## Definition of Done

-   POST payment returns a deliberate `PaymentResponse`.
-   Domain `Payment` is no longer the API contract.
-   Returned JSON is clear and intentional.

------------------------------------------------------------------------

# Ticket S2-16 --- Centralise API Error Handling

## Objective

Extend the API's global error handling so expected banking failures
become useful HTTP responses.

## Existing Exceptions to Review

Your Stage 1 exception hierarchy includes:

``` text
BankingException
├── AccountNotFoundException
├── DuplicateAccountNumberException
├── ExceedsBalanceException
├── InvalidPaymentException
├── InvalidPaymentTransitionException
└── NonPositiveAmountException
```

## Files You May Work With

Your global controller-advice class from S2-08 and an error-response
DTO.

## Requirements

Decide sensible HTTP mappings for expected failures.

Examples to consider:

``` text
unknown resource      → 404
invalid request/input → 400
conflicting operation → possibly 409
```

Do not mechanically map every exception without thinking about its
meaning.

## Implementation Steps

1.  Trigger each relevant failure in ordinary Java/domain code and
    understand what it means.
2.  Decide which failures can reasonably occur through the Stage 2 API.
3.  map those exceptions in the global handler;
4.  use a consistent error-response shape;
5.  test several failure cases manually.

## Things to Think About

-   What distinguishes 400, 404, 409, and 500?
-   Which failures represent client mistakes?
-   Which failures represent missing resources?
-   Why should unexpected programming errors generally not be disguised
    as normal business errors?

## Definition of Done

-   Expected banking failures produce meaningful HTTP statuses.
-   Error bodies use a consistent format.
-   Controllers do not contain repetitive `try/catch` blocks.
-   Unexpected errors are not silently converted into misleading
    success/client responses.

------------------------------------------------------------------------

# Ticket S2-17 --- Revisit Payment Validator Construction

## Objective

Use your new understanding of Spring beans and dependency injection to
review how Stage 1 payment validators are constructed.

This is primarily a design/reasoning ticket.

## Existing Design

The Stage 1 model includes:

-   `PaymentValidator`;
-   `CompositePaymentValidator`;
-   `PositiveAmountValidator`;
-   `CurrencyMatchValidator`;
-   `SufficientFundsValidator`.

`CurrencyMatchValidator` is configured with an expected currency when
constructed.

## Question to Investigate

> Should these validators become Spring-managed beans?

Do not assume the answer is yes.

## Concepts to Review

-   singleton bean;
-   stateless object;
-   configured/stateful object;
-   object ownership;
-   Spring component lifecycle;
-   domain objects versus infrastructure/application components.

## Implementation Steps

1.  Review how validators are currently created.
2.  Identify which validators contain configuration/state.
3.  consider what would happen if one singleton `CurrencyMatchValidator`
    were shared everywhere;
4.  decide whether Spring should manage each validator;
5.  document your reasoning;
6.  make a small refactor only if the reasoning justifies it.

## Things to Think About

-   Does every Java class benefit from `@Component`?
-   Can an ordinary Java object still be created with `new` inside a
    Spring application?
-   Who should own objects whose configuration differs per account?
-   What does singleton scope imply?

## Definition of Done

-   You can explain whether each validator should or should not be
    Spring-managed.
-   Any code change is justified by design rather than annotation
    enthusiasm.
-   Existing payment tests still pass.

------------------------------------------------------------------------

# Ticket S2-18 --- Add REST / Controller Tests

## Objective

Add automated tests for the HTTP layer without replacing the Stage 1
domain tests.

## New Concepts

### MockMvc

MockMvc allows Spring MVC endpoints to be tested without manually using
a browser or external HTTP client for every test.

### Testing Layers

You now have different kinds of behaviour:

``` text
Domain behaviour
    ↓
ordinary JUnit tests

HTTP/controller behaviour
    ↓
Spring MVC tests
```

## Representative Cases

Add a focused set of tests such as:

-   existing account → 200;
-   unknown account → 404;
-   account response contains expected JSON;
-   valid payment → successful response;
-   malformed/invalid payment request → 400;
-   expected banking failure → correct error status.

## Files You Will Work With

Tests should live under:

``` text
src/test/java/com/ehb/banking/
```

with packages mirroring the application structure where useful.

## Implementation Steps

1.  Keep all existing Stage 1 tests.
2.  Add Spring's test support if not already available through your
    dependencies.
3.  begin with one simple controller test;
4.  learn how to perform a GET with MockMvc;
5.  learn how to assert the status;
6.  learn how to inspect/assert JSON;
7.  expand only to the representative cases above.

## Things to Think About

-   What does this test prove that `AccountTest` does not?
-   Why not retest every domain rule through HTTP?
-   When is a controller test more useful than a domain unit test?
-   What parts of Spring are being started for the test?

## Definition of Done

-   Representative REST behaviours are automated.
-   Existing domain tests remain.
-   `mvn test` succeeds.
-   You can explain the difference between domain and web-layer tests.

------------------------------------------------------------------------

# Ticket S2-19 --- Refactor the Stage 2 Package Structure

## Objective

Review the project structure now that the application has genuine layers
and reorganise only where that improves clarity.

Do this **after** the architecture exists rather than trying to predict
the perfect package structure at the beginning.

## Possible Structure

A structure approximately like this may now be appropriate:

``` text
src/main/java/com/ehb/banking/
│
├── EhbBankingApplication.java
├── controller/
├── service/
├── dto/
├── domain/
├── validation/
├── exception/
└── repository/       # temporary in-memory storage only
```

The exact result is not prescribed.

## Requirements

Review:

-   domain classes;
-   validators;
-   exceptions;
-   controllers;
-   services;
-   DTOs;
-   temporary storage;
-   the old `BankingSystem.main()`.

Remove or retire the Stage 1 scratchpad if the Spring Boot application
has fully replaced its purpose.

## Implementation Steps

1.  Inspect the current package tree.
2.  Identify classes whose responsibilities now clearly belong together.
3.  move classes in small groups;
4.  update imports;
5.  compile after each meaningful move;
6.  run all tests;
7.  remove obsolete driver code only when you are sure it is no longer
    useful.

## Things to Think About

-   Is package structure communicating architectural responsibility?
-   Are domain classes still ordinary Java?
-   Have Spring annotations leaked into the domain unnecessarily?
-   Are you refactoring for clarity or merely moving files?

## Definition of Done

-   Package structure is coherent.
-   Application still starts.
-   All tests pass.
-   Obsolete Stage 1 driver code is removed or clearly retired.
-   No behaviour has changed merely because files moved.

------------------------------------------------------------------------

# Ticket S2-20 --- Stage 2 End-to-End Integration Checkpoint

## Objective

Demonstrate and explain the complete Stage 2 application from HTTP
request to domain behaviour and back to JSON.

This is primarily a consolidation ticket rather than a feature ticket.

## Successful Scenario

Run the application and perform a sequence such as:

``` text
Start EHB
    ↓
GET source account
    ↓
GET target account
    ↓
GET source transaction history
    ↓
POST a payment
    ↓
GET source account again
    ↓
GET target account again
    ↓
verify balances changed
    ↓
GET transaction histories
    ↓
verify new transactions exist
```

Also retrieve a business and its accounts if those endpoints were
implemented as specified.

## Failure Scenarios

Deliberately try:

``` text
unknown account
unknown business
blank account number in payment request
zero/negative payment amount
insufficient balance
malformed JSON
```

Confirm that the API responds predictably.

## Architecture Explanation

Without relying on the code as a script, explain:

``` text
HTTP client
    ↓
embedded server
    ↓
Spring MVC
    ↓
Controller
    ↓
Service
    ↓
Domain model
    ↓
DTO
    ↓
Jackson
    ↓
JSON
```

## Questions You Should Be Able to Answer

### Spring / Boot

-   What is Spring?
-   What does Spring Boot add?
-   What is a bean?
-   What is the application context?
-   What is component scanning?
-   What is auto-configuration at a high level?
-   Who starts the embedded server?

### Dependency Injection

-   Who creates your controller?
-   Who creates your service?
-   How does the controller receive the service?
-   Why not use `new AccountService()` in the controller?
-   What is constructor injection?
-   What does inversion of control mean in this project?

### HTTP / REST

-   What do GET and POST mean?
-   What are path variables?
-   What is a request body?
-   What do 200, 400, 404, and 500 broadly mean?
-   Why does an unknown account produce 404?

### Controllers and Services

-   What belongs in a controller?
-   What belongs in a service?
-   What belongs in the domain model?
-   Why should payment rules not live in `PaymentController`?

### DTOs / JSON

-   What is a DTO?
-   Why not return `Account` directly?
-   What is Jackson doing?
-   Why are request and response DTOs different?
-   Why must the `Business` password never be serialised?

### Validation

-   What does `@Valid` do?
-   What is the difference between API validation and domain validation?
-   Why retain domain invariants even when HTTP input is validated?

### Testing

-   What do your Stage 1 domain tests prove?
-   What do the new controller tests prove?
-   Why do you need both?

## Final Commands

Run:

``` bash
mvn test
```

and:

``` bash
mvn clean package
```

Then start the application and perform the manual API checks.

## Definition of Done

Stage 2 is complete when:

-   the Spring Boot application starts reliably;
-   account information can be retrieved through REST;
-   transaction history can be retrieved;
-   business/account relationships can be retrieved;
-   payments can be initiated through REST;
-   request DTOs are validated;
-   response DTOs control API exposure;
-   expected exceptions map to sensible HTTP errors;
-   REST/controller tests pass;
-   all Stage 1 domain tests still pass;
-   the application remains in-memory with no JPA/PostgreSQL;
-   you can explain the complete request lifecycle and the purpose of
    each architectural layer.

------------------------------------------------------------------------

# Stage 2 Scope Check

At the end of Stage 2, the project should contain concepts roughly
equivalent to:

``` text
Spring Boot application
        │
        ├── REST controllers
        │
        ├── services
        │
        ├── request/response DTOs
        │
        ├── validation
        │
        ├── global API exception handling
        │
        ├── temporary in-memory storage
        │
        └── existing Java domain model
```

It should **not yet** contain:

``` text
JPA / Hibernate
PostgreSQL
@Entity
Spring Data repositories
database migrations
Kafka
microservices
Docker
AWS
Spring Security
JWT / OAuth
real login/authentication
```

Those belong to later stages.

------------------------------------------------------------------------

# Recommended Working Method

For each ticket:

1.  Read the entire ticket before coding.
2.  Ask for an explanation of any concept that is unfamiliar.
3.  Implement only the current ticket.
4.  Compile/run frequently rather than writing everything before
    testing.
5.  If an error occurs, investigate that error before adding more code.
6.  Run the relevant tests.
7.  Explain the new concept back in your own words.
8.  Commit the completed ticket to Git.
9.  Move to the next ticket.

The goal is not to finish Stage 2 as quickly as possible. The goal is
that, by the end, Spring Boot no longer feels like a collection of
unexplained annotations.

------------------------------------------------------------------------

# Stage 2 Progress Checklist

``` text
[ ] S2-01 — Add Spring Boot to the Existing Maven Project
[ ] S2-02 — Create the Spring Boot Application Entry Point
[ ] S2-03 — Create the First REST Endpoint
[ ] S2-04 — Create Simple In-Memory Account Storage
[ ] S2-05 — Introduce AccountService and Dependency Injection
[ ] S2-06 — Retrieve an Account Through REST
[ ] S2-07 — Introduce an Account Response DTO
[ ] S2-08 — Handle Unknown Accounts with HTTP 404
[ ] S2-09 — Expose Account Transaction History
[ ] S2-10 — Expose Businesses and Their Accounts
[ ] S2-11 — Create the Payment Request DTO
[ ] S2-12 — Validate Incoming Payment Requests
[ ] S2-13 — Introduce PaymentService
[ ] S2-14 — Create the Payment POST Endpoint
[ ] S2-15 — Create the Payment Response DTO
[ ] S2-16 — Centralise API Error Handling
[ ] S2-17 — Revisit Payment Validator Construction
[ ] S2-18 — Add REST / Controller Tests
[ ] S2-19 — Refactor the Stage 2 Package Structure
[ ] S2-20 — Stage 2 End-to-End Integration Checkpoint
```

Once S2-20 is complete, the project is ready to move into **Stage 3:
JPA, PostgreSQL, persistence, and the next layer of testing**.
