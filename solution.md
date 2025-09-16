### Solution

A small Spring Boot 3 (Java 17) application that ingests the provided Awards CSV, persists it to MySQL, and exposes a
small REST API to:

1) Check whether a movie won a category (default: Best Picture) using the CSV as a source of truth and fetch movie
   metadata from OMDb (cached)
2) submit ratings for a movie based on its imdbID
3) List the top-10 rated movies sorted by box office value.

Upon application startup, csv file is ingested once (based on checksum and if database is empty)

Design focuses on clarity, testability and maintainability:

- Controllers are lightweight and depend on services
- Services depend only on related repositories and other dependencies
- Services are designed based on Single Responsibility Principle
- Modern WebClient is used for OMDB calls
- JPA for data persistence

The below endpoints are exposed to satisfy requirements:

- /api/movies/winner
- /api/movies/rate
- /api/movies/top-rated

*More info can be found under swagger:* >/swagger-ui.html

### Postman

> Along with the project code there's a postman collection for easy import under *postman_collection* directory

### Additional References

[How to run](how_to_run.md)