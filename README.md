# Land Ownership Application
Land Tech land ownership challenge part completed to time allocated.

Please check out the [Decision Log](DECISION_LOG.md), for details on my approach.

# Developer Setup
## Dependencies
1. __Java JDK 11__ - for compiling/runnning Java software
    * Either download the JDK from Oracle, 
    * Alternatively, use Jabba Java version manager
2. __Maven 3__ - for build and dependencies
    * Can be installed via Homebrew
3. __IntelliJ CE__ / any mature Java IDE
    * For importing project for continued development
    
## Running Tests
Tests can be run from the IDE if imported as a Maven project.

Alternatively from the shell:

```
mvn clean test
```

## Running Application
Right now, the company data is hard coded and is currently only company IDs. These can be 
found in the Application.java class.

Ways to run:

__Use Maven to run the application__

```
mvn clean compile exec:java -Dexec.mainClass=tech.land.ownership.Application -Dexec.args="C45353"
```

__Run a standalone executable jar package__

```
mvn clean package
java -jar target/land-ownership-1.0-SNAPSHOT.jar "C45353"
```

# Decision Log

[Decision Log File](DECISION_LOG.md)