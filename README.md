# ModResorts Demo Application

## Overview
ModResorts (as per the `main` branch) is a IBM WebSphere Application Server web application. It is a simple application that can be used to demonstrate application modernization from IBM WebSphere Application Server traditional to Liberty, which is a modern cloud-ready enterprise Java runtime, as well as Java SE version upgrade scenarios.
The Java source code is dependent on APIs that only exist on the IBM WebSphere Application Server and as such, this version of the application will only function correctly when deployed to IBM WebSphere Application Server. In order to successfully deploy to Liberty, code changes need to be made to the application. See [Liberty Versions of ModResorts](#liberty-versions-of-modresorts) below.


## Building

### IBM WebSphere Application Server Dependencies
The `main` branch version of ModResorts has dependencies on WebSphere Application Server APIs. The `pom.xml` references the associated WAS dependency and to build the application, you will need to have the dependency available in a maven repository. The `was_public.jar` jar and its associated `pom` file can be found in your WebSphere installation. For example, in a typical installation, you might find them at the following location: `/opt/WebSphere/AppServer/dev`.
You can install to your local maven repository (`$HOME/.m2`) using the following command:

```
mvn install:install-file -Dfile=<some location>/was_public.jar -DpomFile=<some location>/was_public-9.0.0.pom
```

For more information please see the [docs](https://www.ibm.com/docs/en/wasdtfe?topic=environment-installing-server-apis-into-maven-repository).

### Building Using Maven
This is a standard single module maven application and the WAR can be built as follows:

```
mvn clean package
```


### Building Using Gradle
This application can also be built using gradle:

```
./gradlew clean build
```

## Liberty Versions of ModResorts
Two Liberty versions of the application are maintained on the following branches:

- `liberty-java8`
  This branch shows what the application looks like after it has been modernized to Liberty. Comparing this branch to main, you will notice the following changes:
  - Code changes in some source files (to remove use of WAS APIs)
  - Addition of the Liberty config file: `src/main/liberty/config/server.xml`. This file is produced by IBM Application Modernization Accelerator and is available in the [migration plan](#migration-plan)
  - A `Containerfile` has also been added to the project root to allow you to build an image and run the application in a container.
  - [Liberty Maven Plugin](https://github.com/OpenLiberty/ci.maven) has been configured in the `pom.xml` for convenience of developing the application with Liberty. 

- `liberty-java21`
  This branch shows what the application looks like after it has been modernized to Liberty **AND** upgraded to Java 21. Comparing this branch to main, you will notice all the changes described for the `liberty-java8` branch in addition to:
  - Code changes in some source files (to fix Java upgrade issues)

## Version 2
Version 2 of ModResorts contains extra migration issues that need to be addressed when modernizing to Liberty and when upgrading Java. Version 2 can be used for a more complete modernization demo.

Version 2 exists in branches that represent the various stages of modernization:

- `main-v2` 
  This branch captures the application as a traditional WebSphere Application Server application built with and running on Java 8

- `liberty-java8-v2`
  This branch captures the application after it has been modernized to Liberty, but still built with and running on Java 8

- `liberty-java21-v2`
  This branch captures the final state of the application after it has been modernized to Liberty, and upgraded to use Java 21.

In order to build and run Version 2 of the application, you need to install the dependencies in the `dependencies` directory to your local maven repository:

```
mvn install:install-file -Dfile=dependencies/env-config-1.5.jar -DpomFile=dependencies/env-config-1.5.pom
mvn install:install-file -Dfile=dependencies/env-config-1.6.jar -DpomFile=dependencies/env-config-1.6.pom
mvn install:install-file -Dfile=dependencies/env-config-1.7.jar -DpomFile=dependencies/env-config-1.7.pom
```


## Deploying the Application to IBM WebSphere Application Server
There are no special instructions for deploying the application to IBM WebSphere Application Server. There is no configuration required on the application server in order for the application to deploy and function.

It can be deployed using the UI console or using `wsadmin`.
See the [documentation](https://www.ibm.com/docs/en/was-nd/9.0.5?topic=applications-how-do-i-deploy) for more details on deploying the application to WebSphere Application Server.



## Deploying the Application to Liberty
To deploy the application on Liberty you can do one of the following:
- Install the Liberty Tools IDE plugin (available for VS Code, Eclipse IDE and IntelliJ IDEA)
- Add Liberty Maven or Gradle Plugin configuration to your build file. Note, for convenience, configuration for Liberty Maven Plugin is already added to the `pom.xml` in the `liberty-` branches. Liberty can be launched in dev mode with the following command:
```
mvn liberty:dev
```
- Start Liberty in dev mode directly from command line:
```
mvn io.openliberty.tools:liberty-maven-plugin:3.10.2:dev
```
- Build and drop the WAR file into Liberty installation.

For more on Liberty Tools, see [Develop with Liberty Tools](https://openliberty.io/docs/latest/develop-liberty-tools.html)

## Building and Running the Application a Liberty Container
A Containerfile exists in the `liberty-` branches. The Containerfile is produced by IBM Application Modernization Accelerator and is available in the [migration plan](#migration-plan). It can be used to build an image and run the application in a container. You can build the image as follows:

```
docker build -t modresorts:latest -f Containerfile .
```

You can run the container as follows:

```
docker run --rm -d -p 9080:9080 modresorts:latest
```

## Migration Plan
The `migration_bundle` directory contains a migration plan for ModResorts created by [IBM Application Modernization Accelerator](https://www.ibm.com/products/jsphere/tools) (AMA). It is used by IBM AMA Dev Tools (available for VS Code, Eclipse IDE, and IntelliJ IDEA) or IBM Bob to accelerate modernization of legacy enterprise Java applications to run on Liberty.  [Try](https://www.ibm.com/account/reg/us-en/signup?formid=urx-53705) AMA.
