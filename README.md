# Revolut Backend Test


### Requires

- Java 10.0.2+
- Maven of any version which supporting java 10 (Maven should also be executed under java 10)


### How to build
- Navigate to project main directory (accounts-test)
- Run ```mvn clean package``` to run tests and create executable jar
- Run ```java -jar target/accounts-test-1.0-jar-with-dependencies.jar``` to launch application

### Dependencies

* Spark for web
* Gson for json
* H2 + Hikari for DB
* Apache httpclient + fluent mostly for test purposes
* Junit 4 for testing.