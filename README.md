# FileManagerApp

A simple Spring Boot app that I created for practicing purposes. 
The app represents a file storage app with different permissions for different users.
Full functional can be viewed on http://localhost:8080/swagger-ui/index.html# using swagger-ui.

## Technologies:
- Java
- Spring Boot
- PostgreSQL

### Dependencies
- Liquibase
- Swagger
- MinIo

## How to run

### Run using docker
```
docker-compose up -p
```

### Run using maven
```
mvn spring-boot:run
```
Note that if you run it with maven you will have to also start the MinIO server manually.
