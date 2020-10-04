# Land Ownership Application
Land Tech land ownership challenge part completed to time allocated.

Please check out the [Decision Log](DECISION_LOG.md), for details on my approach.

# Developer Setup
## Dependencies
1. Java JDK 11 - for compiling/runnning Java software
2. Maven 3 - for build and dependencies - can be installed via homebrew
3. IntelliJ CE / any mature Java IDE - for importing project for continued development

## Running Tests
Tests can be run from the IDE if imported as a Maven project.

Alternatively from the shell:

```
mvn clean test
```

## Running Application
Right now, the company data is hard coded and is currently only company IDs. These can be 
found in the Application.java class.

Some options to run:

1. Use Maven to run the application:

```
mvn clean compile exec:java -Dexec.mainClass=tech.land.ownership.Application -Dexec.args="C45353"
```

2. Build a standalone executable jar package and run that:

```
mvn clean package
java -jar target/land-ownership-1.0-SNAPSHOT.jar "C45353"
```

# Decision Log

[Decision Log File](DECISION_LOG.md)