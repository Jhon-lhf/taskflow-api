# TaskFlow API

A RESTful API for project and task management, inspired by tools like Jira. Built with Spring Boot, JWT authentication,
and PostgreSQL.

---

## Tech Stack

| Layer       | Technology                        |
|-------------|-----------------------------------|
| Language    | Java 17                           |
| Framework   | Spring Boot 3.5.11                |
| Security    | Spring Security + JWT (jjwt 0.12) |
| Persistence | Spring Data JPA + Hibernate       |
| Database    | PostgreSQL                        |
| Build tool  | Maven                             |
| Utilities   | Lombok                            |

---

## Running Locally

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 14+

### 1. Clone the repository

```bash
git clone https://github.com/your-username/taskflow-api.git
cd taskflow-api
```

### 2. Create the database

```sql
CREATE
DATABASE taskflow_db;
CREATE
USER dev_user WITH PASSWORD 'devpass123';
GRANT ALL PRIVILEGES ON DATABASE
taskflow_db TO dev_user;
```

### 3. Configure environment variables

The application reads sensitive values from environment variables. You can set them in your terminal or create a `.env`
file:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/taskflow_db
export DB_USERNAME=dev_user
export DB_PASSWORD=devpass123
export JWT_SECRET=4A614E645267556B58703273357638792F423F4528482B4D6251655468576D5A
```

> The application includes fallback defaults for local development, so this step is optional if you use the exact values
> above.

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The API will start on `http://localhost:8080`.

On first startup, the `DataInitializer` automatically seeds the database with the system roles: `ADMIN`,
`PROJECT_MANAGER`, and `DEVELOPER`.

---

## API Endpoints

All endpoints are prefixed with `/api/taskflow/v1`.

### Auth

| Method | Endpoint         | Auth required | Description                                                |
|--------|------------------|---------------|------------------------------------------------------------|
| `POST` | `/auth/register` | No            | Register a new user (assigned `DEVELOPER` role by default) |
| `POST` | `/auth/login`    | No            | Login and receive a JWT token                              |

**Register request body:**

```json
{
  "username": "jhonatan",
  "email": "jhonatan@example.com",
  "password": "secret123"
}
```

**Login request body:**

```json
{
  "username": "jhonatan",
  "password": "secret123"
}
```

**Login response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

> Use the token in all subsequent requests as: `Authorization: Bearer <token>`

---

### Projects

| Method | Endpoint         | Auth required                           | Description          |
|--------|------------------|-----------------------------------------|----------------------|
| `POST` | `/projects`      | Yes — `ADMIN` or `PROJECT_MANAGER` role | Create a new project |
| `GET`  | `/projects/{id}` | Yes                                     | Get a project by ID  |

**Create project request body:**

```json
{
  "name": "My First Project",
  "description": "A project to track tasks"
}
```

> The authenticated user is automatically added as `LEADER` of the project.

---

### Tasks

| Method   | Endpoint                | Auth required | Description                    |
|----------|-------------------------|---------------|--------------------------------|
| `POST`   | `/tasks`                | Yes           | Create a task inside a project |
| `GET`    | `/tasks/{id}`           | Yes           | Get a task by ID               |
| `GET`    | `/tasks?projectId={id}` | Yes           | Get all tasks for a project    |
| `PATCH`  | `/tasks/{id}/status`    | Yes           | Update the status of a task    |
| `DELETE` | `/tasks/{id}`           | Yes           | Delete a task                  |

**Create task request body:**

```json
{
  "title": "Implement login screen",
  "description": "Design and code the login UI",
  "priority": "HIGH",
  "projectId": 1,
  "assignedTo": "jhonatan",
  "dueDate": "2025-12-31"
}
```

**Update task status:**

```
PATCH /tasks/1/status?status=IN_PROGRESS
```

Valid status values: `TODO`, `IN_PROGRESS`, `DONE`

---

### Project Members

| Method   | Endpoint                                     | Auth required                  | Description                   |
|----------|----------------------------------------------|--------------------------------|-------------------------------|
| `POST`   | `/projects/{id}/members`                     | Yes — must be project `LEADER` | Add a member to the project   |
| `GET`    | `/projects/{id}/members`                     | Yes                            | List all members of a project |
| `PUT`    | `/projects/{id}/members`                     | Yes — must be project `LEADER` | Update a member's role        |
| `DELETE` | `/projects/{id}/members?username={username}` | Yes — must be project `LEADER` | Remove a member               |

**Add / update member request body:**

```json
{
  "username": "maria",
  "role": "DEVELOPER"
}
```

Valid roles: `LEADER`, `DEVELOPER`, `VIEWER`

> Business rules: The last `LEADER` of a project cannot be removed or have their role changed. This prevents a project
> from becoming leaderless.

---

## Error Handling

All errors follow the [RFC 9457 Problem Detail](https://www.rfc-editor.org/rfc/rfc9457) standard:

```json
{
  "type": "about:blank",
  "title": "Resource not found",
  "status": 404,
  "detail": "Task not found with ID 99",
  "timestamp": "2024-11-01T10:30:00"
}
```

| Status | Scenario                                         |
|--------|--------------------------------------------------|
| `400`  | Validation error or invalid input                |
| `401`  | Missing or invalid JWT token                     |
| `403`  | Authenticated but not authorized for this action |
| `404`  | Resource not found                               |
| `409`  | Conflict (e.g. user already a project member)    |
| `500`  | Unexpected server error                          |

---

## Project Structure

```
src/main/java/com/jhonatan/taskflow/
│
├── config/              # Spring Security and application beans
├── controller/          # REST controllers (entry points)
├── domain/
│   ├── entity/          # JPA entities (mapped to DB tables)
│   └── enums/           # TaskStatus, TaskPriority, ProjectRole
├── dto/                 # Request and response data transfer objects
├── exception/           # Custom exceptions and global handler
├── mapper/              # Entity ↔ DTO conversion
├── repository/          # Spring Data JPA repositories
├── security/            # JWT filter, JwtService, CustomUserDetails
└── service/
    ├── impl/            # Service implementations
    └── *.java           # Service interfaces
```

---

## Upcoming Features

- [ ] **Unit and integration tests** — service layer tests with JUnit 5 + Mockito, and integration tests with
  `@SpringBootTest`
- [ ] **Pagination and filtering** — paginate task lists and filter by status, priority, or assignee
- [ ] **User management** — update profile, change password, list users (admin only)
- [ ] **Refresh token** — issue short-lived access tokens with long-lived refresh tokens
- [ ] **Task comments** — allow members to comment on tasks
- [ ] **Deployment** — Docker Compose setup and cloud deployment guide

---

## Author

**Jhonatan** — [GitHub](https://github.com/your-username)
