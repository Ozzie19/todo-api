# ToDo API

A RESTful ToDo API built with Spring Boot, PostgreSQL, and Docker, featuring full CRUD operations. The project demonstrates modern backend development practices, containerisation, and deployment via Nginx reverse proxy.

---

## Features

- ✅ Create, Read, Update, Delete ToDo items
- ✅ Dynamic Postman tests for all endpoints
- ✅ PostgreSQL database persistence
- ✅ Fully containerized with Docker and Docker Compose
- ✅ Deployed behind an Nginx reverse proxy
- ✅ Live demo available 

---

## Tech Stack

-   **Backend:** Java, Spring Boot
-   **Database:** PostgreSQL
-   **Containerization:** Docker, Docker Compose
-   **Reverse Proxy:** Nginx
-   **Testing:** Postman (with automated environment variables and dynamic test data)

---

## Getting Started

### Prerequisites

-   Docker & Docker Compose installed
-   Java 17+ (for local development without Docker)
-   Git

### Running Locally with Docker

1.  **Clone the repository:**
    ```bash
    git clone (https://github.com/Ozzie19/todo-api.git)
    cd todo-api
    ```
2.  **Copy environment variables:**
    ```bash
    cp .env.example .env
    ```
3.  **Update `.env`** with your PostgreSQL credentials if needed.

4.  **Start the stack:**
    ```bash
    docker-compose up -d
    ```
5.  **Access the API:**
    -   API base URL: `http://localhost:8080/api/todo`
    -   Example: `GET /api/todo` to fetch all tasks

---

## API Endpoints

| Method | Endpoint         | Description                  |
| :----- | :--------------- | :--------------------------- |
| `GET`  | `/api/todo`      | Retrieve all ToDo items      |
| `GET`  | `/api/todo/{id}` | Retrieve a ToDo by ID        |
| `POST` | `/api/todo`      | Create a new ToDo item       |
| `PUT`  | `/api/todo/{id}` | Update an existing ToDo item |
| `DELETE`|`/api/todo/{id}` | Delete a ToDo item           |

---

## Postman Testing

The project includes automated Postman tests to verify all API endpoints.

-   Import `ToDo.postman_collection.json` into Postman.
-   Set up a Postman environment with `{{baseUrl}}` pointing to your API URL.
-   The tests dynamically generate titles and validate CRUD operations.
-   Run the collection to verify API functionality.

### Example Screenshots

-   **Postman Collection Tests:**
    -   **Dynamic Title Generation Example:**
    ---

## Deployment

The API is deployed using Docker and Docker Compose.
-   PostgreSQL runs as a separate container.
-   Nginx reverse proxy handles domain routing, enabling multiple services on the same server.
-   **Live demo:** `http://ozgur-todo.mooo.com/api/todo`

---

## Notes

-   Every `POST` request generates a dynamic title for testing purposes.
-   Error handling for deleted or missing resources can be improved (404 responses are tested in Postman).
-   Postman tests ensure that all endpoints behave as expected, even with dynamic input.

---

## Contact

Created by Ozgur Dorunay
-   GitHub: `https://github.com/Ozzie19/todo-api`
