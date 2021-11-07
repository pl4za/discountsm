# discountsm

Deals, discounts and vouchers.

## Setup

### PostgreSQL DB:

docker pull postgres:13 docker run --name dev-postgres -p 5432:5432 -e
POSTGRES_HOST_AUTH_METHOD=trust -d postgres:13 docker exec dev-postgres psql -U postgres -c "CREATE
DATABASE circle_test" postgres

### Other

Fix migration mvn -Dflyway.user=postgres -Dflyway.url=jdbc:postgresql://localhost/circle_test
flyway:clean

[![CircleCI](https://circleci.com/gh/pl4za/discountsm.svg?style=shield&circle-token=84d6308b028d53b580577374a553049c3b831af3)](https://app.circleci.com/pipelines/github/pl4za/discountsm)
