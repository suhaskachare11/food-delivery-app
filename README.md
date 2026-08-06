# QuickBite — Enterprise-grade Food Delivery Platform

A modular monolith backend built with Spring Boot, designed to grow into microservices over time.

---

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.5**
- **PostgreSQL 17** (via Docker)
- **Flyway** — database migrations
- **Spring Data JPA** — persistence
- **Spring Security** — access control
- **springdoc-openapi** — Swagger UI / API docs
- **Lombok**
- **Maven**
- **docker**

---

## Prerequisites

Make sure the following are installed before you start:

| Tool | Version | Notes |
|---|---|---|
| Java (JDK) | 21 | Check with `java -version` |
| Docker Desktop | Latest | Must be running |
| Git | Latest | — |

Maven itself is **not** required separately — this project uses the Maven Wrapper (`mvnw` / `mvnw.cmd`), which downloads the correct Maven version automatically.

---

## Getting Started (Local Setup)

### 1. Clone the repository

```bash
git clone <repo-url>
cd food-delivery-app
git checkout develop
```

If you're going to make changes, create your own feature branch off `develop` rather than committing directly to it:

```bash
git checkout -b feature/your-feature-name
```

### 2. Set up environment variables

Copy the example env file and adjust if needed:

```bash
cd infrastructure
copy .env.example .env      # Windows
# cp .env.example .env      # Mac/Linux
```

The `.env` file configures your local Postgres container (DB name, user, password, port). Default values work out of the box for local dev.

### 3. Start PostgreSQL

From the `infrastructure/` folder:

```bash
docker compose up -d
```

This starts a Postgres 17 container named `quickbite-postgres`, exposed on `localhost:5432`.

Verify it's running:

```bash
docker ps
```

You should see `quickbite-postgres` with status `Up`.

> **Resetting the database:** if you ever need a completely clean slate (e.g. after schema/migration conflicts), run `docker compose down -v` (removes the data volume) followed by `docker compose up -d` again.

### 4. Run the Spring Boot application

From the `backend/` folder:

```bash
cd ../backend
./mvnw spring-boot:run          # Mac/Linux
.\mvnw.cmd spring-boot:run      # Windows
```

On startup, **Flyway automatically applies all database migrations** — no manual SQL needed. You'll see log lines confirming migration and successful Hikari connection pool startup.

The app runs on: `http://localhost:8080`

### 5. Explore the API via Swagger UI

Once the app is running, open:

```
http://localhost:8080/swagger-ui/index.html
```

All endpoints are documented here and can be tested directly with "Try it out" — no Postman/curl required.

---

## Project Structure

```
food-delivery-app/
├── backend/
│   └── src/main/java/com/quickbite/fooddelivery/
│       ├── FoodDeliveryApplication.java
│       ├── common/
│       │   ├── config/          # SecurityConfig, OpenApiConfig, etc.
│       │   └── exception/       # GlobalExceptionHandler
│       └── user/                # User module (self-contained)
│           ├── User.java
│           ├── UserRepository.java
│           ├── UserService.java
│           ├── UserController.java
│           ├── UserNotFoundException.java
│           └── dto/
│               ├── UserRequest.java
│               └── UserResponse.java
│   └── src/main/resources/
│       ├── application.properties
│       └── db/migration/        # Flyway SQL migrations (V1, V2, ...)
├── infrastructure/
│   ├── docker-compose.yml
│   ├── .env.example
│   └── .env                     # local only, gitignored
└── README.md
```

**Design principle:** each business domain (user, order, restaurant, etc.) lives in its own top-level package with everything it needs — entity, repository, service, controller, DTOs. Modules should not reach into each other's internals directly. This keeps the codebase easy to split into microservices later.

---

## Database Migrations (Flyway)

Migration files live in `backend/src/main/resources/db/migration/`, named:

```
V<version>__<description>.sql
```

Example:
```
V1__create_users_table.sql
V2__create_restaurants_table.sql
```

**Rules:**
- Never edit or delete a migration file once it has been applied to any shared database.
- To change a table after the fact, write a **new** migration (e.g. `V3__add_phone_to_users.sql`) instead of editing an old one.
- All modules share one migration folder and one continuous version sequence, since they share a single database in this monolith.

---

## API Endpoints (User Module)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/users` | Create a new user |
| GET | `/api/v1/users` | Get all users |
| GET | `/api/v1/users/{id}` | Get a user by ID |

Full request/response schemas are available in Swagger UI.

---

## Troubleshooting

**App fails with "Failed to determine a suitable driver class"**
→ Postgres isn't running, or `spring.datasource.*` properties are missing/incorrect in `application.properties`. Run `docker ps` to confirm the container is up.

**Flyway checksum mismatch error on startup**
→ A migration file was edited after being applied. Reset your local DB: `docker compose down -v && docker compose up -d`.

**Swagger UI redirects to a login page**
→ Confirm `SecurityConfig.java` exists in `common/config/` and permits `/swagger-ui/**` and `/v3/api-docs/**`.

**`role "..." does not exist` when connecting to Postgres**
→ The Docker volume was created before `.env` had the right values. Reset with `docker compose down -v && docker compose up -d`.

---

## Contributing

1. Create a feature branch off `develop`
2. Make your changes, following the modular folder structure above
3. Add a Flyway migration for any schema changes
4. Open a PR with a clear description