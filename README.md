# Java Technical Exercise

## How to run

### Environment requirements

To run application the following needs to be installed:

- Docker
- Maven
- Java 21

### Launch steps

Follow the steps below to start up in the order they are listed:

1. Build and run Docker image
    ```shell
    docker build -t dvdrental .
    docker run -p 5432:5432 --name dvdrental -d dvdrental
    ```
2. Build and run application
    ```shell
    mvn clean install
    mvn spring-boot:run
    ```
3. Open in browser [Swagger](http://localhost:8080/swagger-ui/index.html) page and call `/api/v1/film/recommendations`
   endpoint with needed `customerId` as a request param. Endpoint should return the list of recommended films according
   to the task description.

## Implementation notes

Application consists of 3 components:

- `FilmRecommendationController` implements API of application which acts as a frontend
- `FilmService` implements a business logic such as parameter validation and making calls to repository
- `FilmRepository` implements logic of communication with database. `JdbcTemplate` is used.

### SQL

Task description says that the films must be returned in the following order:

1. (primary) In descending order, the number (i.e. count) of films that the target customer has rented with the same
   category as this film
2. (secondary) In descending order, the number (i.e. count) of times that this film has been rented across all customers
3. (tertiary) In ascending order, title alphabetically.

To meet first 2 requirements temporary tables `customer_rented_categories` and `film_rental_count` created using CTEs.
Joining this tables allow to apply required sorting.

Another requirement is films that have already been rented by the target customer must be omitted from the list of
recommendations.
To meet this requirement `NOT EXISTS` is used. It consumes the result set of customer rented films subquery which allows
to filter the final result set.

### Tests

To prove the solution integration test is implemented. It runs Testcontainer with `dvdrental` image.
Makes call to `/api/v1/film/recommendations?customerId=1` using `TestRestTemplate` and makes sure that output is equal
to [expected](customer-1-recommended-films.json).

## Task description

We would like you to implement a film recommendation service on top of the PostgreSQL DVD rental sample database.

![](./dvdrental-er-diagram.png)

### Requirements

The objective of this exercise is to implement an application that returns a list of 10 film recommendations for a given
customer.
The `customer_id` of the target customer can be passed as the input.

### Output

Exactly ten films must be returned as a JSON array. Each JSON film object must have the following properties:

| JSON property | Database column    |
|---------------|--------------------|
| `id`          | `film.film_id`     |
| `title`       | `film.title`       |
| `description` | `film.description` |
| `category`    | `category.name`    |
| `rating`      | `film.rating`      |
| `length`      | `film.length`      |

The films must be returned in the following order:

1. (primary) In descending order, the number (i.e. count) of films that the target customer has rented with the same
   `category` as this film
2. (secondary) In descending order, the number (i.e. count) of times that this film has been rented across all customers
3. (tertiary) In ascending order, `title` alphabetically

The limit of ten must be applied after this ordering has been applied to the full result set.

Films that have already been rented by the target customer must be omitted from the list of recommendations.

The expected output for `customer_id 1` can be viewed [here](customer-1-recommended-films.json). This example has been
formatted for readability, but doing so is not a requirement.

### Assessment

- Good documentation to get us started and understand your solution is important.
- Your project must build and run on Linux x86 or macOS ARM64.
- How you run the solution is up to you - a simple command line/stdout solution is perfectly fine; a Docker image can be
  provided if desired. We just ask that you include clear instructions on how to run the application.
- Tests to prove your solution are vital.