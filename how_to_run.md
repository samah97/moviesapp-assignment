## How to run

There are two ways to run the application.

### Docker Option (Recommended)

This option runs the app via Docker.

##### Pre-requisite

1) Docker
2) OMDB API Key which can be created [here](https://www.omdbapi.com/apikey.aspx)

##### Steps:

1. Navigate to the project's directory
2. Modify docker-compose.yml to set:
    - OMDB_API_KEY
    - APP_API_TOKEN
3. Run: docker compose up -d --build

That's it!

The application will be running under:

[http://localhost:8080/api](http://localhost:8080/api)

### Maven Option

This option runs the app maven command line.

##### Pre-requisite

1) Java JRE (17)
2) Mysql server
3) OMDB API Key which can be created [here](https://www.omdbapi.com/apikey.aspx)

##### Steps:

1. Navigate to the project's directory
2. The following environment variables should be passed or edit application.yml:
    - spring.datasource.url
    - DB_USER
    - DB_PASSWORD
    - OMDB_API_KEY
    - APP_API_TOKEN
2. Run the command:</br>
   <code>mvn spring-boot:run \
   -Dspring-boot.run.arguments="--DB_USER=*{db_user}* --DB_PASSWORD=*{db_pass}* --OMDB_API_KEY=*{your_api_key_here}*
   --APP_API_TOKEN=*{custom-token}*"
   </code>

The application will be running under:
[http://localhost:8080/api](http://localhost:8080/api)

## Actuator

To check application health you can navigate to:

[http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

## Swagger

API documentation can be found under the following swagger:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

