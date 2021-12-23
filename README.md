# discountsm (beta)

_Deals, discounts and vouchers_

[![CircleCI](https://circleci.com/gh/pl4za/discountsm.svg?style=shield&circle-token=84d6308b028d53b580577374a553049c3b831af3)](https://app.circleci.com/pipelines/github/pl4za/discountsm)

## Features (V0.1)

- See deals
- Create deals
- Vote on deals
- Share deals

## Tech

- Spring boot
- Hibernate
- Postgres
- Flyway
- Docker

## Running the service

This service and its integration tests require a postgres database running on port 5432.
The below docker command will spin up the service (8080) and the database (5432).
```
docker build --tag discountsm .
docker-compose up
```
The postgres docker container is mapped to port 5432, so you can stop the service in docker and run it manually using:

```mvn spring-boot:run```

## Other

### Fix migrations after changes:

```
mvn -Dflyway.user=postgres -Dflyway.url=jdbc:postgresql://localhost/discountsm flyway:clean
```