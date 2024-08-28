Sure, here's a basic README file for your project:

# User Service

This is a RESTful API for managing users. It uses Spring Boot and Reactor for reactive programming.

## Endpoints

### GET /api/users/{id}

Get a user by their ID.

#### Parameters

- `id` (path): The ID of the user to get.

#### Responses

- `200 OK`: The user was found and returned.
- `404 Not Found`: The user was not found.
- `500 Internal Server Error`: An unexpected error occurred.

### GET /api/users

Get all users.

#### Responses

- `200 OK`: The users were found and returned.
- `500 Internal Server Error`: An unexpected error occurred.

### POST /api/users

Create a new user.

#### Request Body

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123"
}
```

#### Responses

- `201 Created`: The user was created and returned.
- `400 Bad Request`: The user data provided is invalid.
- `500 Internal Server Error`: An unexpected error occurred.

### PUT /api/users/{id}

Update a user.

#### Parameters

- `id` (path): The ID of the user to update.

#### Request Body

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123"
}
```

#### Responses

- `200 OK`: The user was updated and returned.
- `400 Bad Request`: The user data provided is invalid.
- `404 Not Found`: The user was not found.
- `500 Internal Server Error`: An unexpected error occurred.

### DELETE /api/users/{id}

Delete a user.

#### Parameters

- `id` (path): The ID of the user to delete.

#### Responses

- `204 No Content`: The user was deleted.
- `404 Not Found`: The user was not found.
- `500 Internal Server Error`: An unexpected error occurred.

## Development

To run the project locally, you'll need to have Java and Maven installed. Clone the repository and run the following commands:

```
mvn clean install
mvn spring-boot:run
```

This will start the server on `http://localhost:8080`. You can then use a tool like Postman to test the endpoints.
