# Michele's app to Test Obnoxiously Large SQL Queries

This docker-compose setup is made of:

1. A Spring App
2. A PostgreSQL database

The Spring app, every time it receives an `HTTP GET` request on `http://<docker-machine-ip>:8080/api/greetings`,
fires a 5200+ characters long query to the unsuspecting PostgreSQL database.

The setup comprises a dockerized Instana agent for maximum comfort.

## Build and run

Enter a valid `INSTANA_AGENT_KEY` in the `docker-compose.yml` file, then:

```bash
$ ./mvnw clean package -DskipTests
$ docker-compose build
$ docker-compose up
```

After the agent is all set up:

```bash
$ curl http://$(docker-machine ip):8080/api/greetings
```