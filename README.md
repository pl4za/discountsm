# discountsm

_Deals, discounts and vouchers_

[![CircleCI](https://circleci.com/gh/pl4za/discountsm.svg?style=shield&circle-token=84d6308b028d53b580577374a553049c3b831af3)](https://app.circleci.com/pipelines/github/pl4za/discountsm)

## Features

- Share deals
- Vote on deals
- Login / Register
- Save deals
- Comment on deals

## Tech

- Spring boot
- Hibernate
- Postgres
- Flyway
- Joda Time/Money

## Running the service

This service and its integration tests require a postgres database running on port 5432.

### Auto (Docker)

```
docker build --tag discountsm .
docker-compose up
```

### Manual

```
docker pull postgres:14.1-alpine 
docker run --name postgres -p 5432:5432 -e POSTGRES_HOST_AUTH_METHOD=trust POSTGRES_DB=circle_test -d postgres:14.1-alpine 
```

And simply run it locally with:
```mvn spring-boot:run```

## Other

### Fix migrations after changes:

```
mvn -Dflyway.user=postgres -Dflyway.url=jdbc:postgresql://localhost/circle_test flyway:clean
```