# Kotlin Coroutines + Spring Data R2DBC + GraphQL API

This project demonstrates a backend setup using **Kotlin Coroutines**, **Spring Data R2DBC**, and a **GraphQL API** to manage employee data. The data model stores employee details, and the GraphQL API provides queries and mutations for interacting with this data. This project is built using **Kotlin 1.9.25** and **JDK 21**.

## Table of Contents

- [Project Overview](#project-overview)
- [Technologies Used](#technologies-used)
- [Project Setup](#project-setup)
- [Data Model](#data-model)
- [GraphQL Schema](#graphql-schema)
- [GraphQL Queries](#graphql-queries)
- [GraphQL Mutations](#graphql-mutations)
- [Pagination and Sorting](#pagination-and-sorting)
- [Running the Project](#running-the-project)

## Project Overview

This backend system provides a GraphQL API to manage employee data. The system supports listing employees with optional filters, retrieving details of a specific employee, and supporting CRUD operations (Add and Update). It also includes pagination and sorting for efficient data retrieval.

## Technologies Used

- **Kotlin 1.9.25**: The language used for the backend.
- **Spring Data R2DBC**: To interact with the PostgreSQL database using reactive programming.
- **Spring Boot 3.x**: For setting up the backend application.
- **GraphQL**: For querying and mutating employee data.
- **Kotlin Coroutines**: For handling asynchronous operations in a non-blocking manner.
- **JDK 21**: Java Development Kit used for running the application.

## Project Setup

To get started with this project:

1. Clone this repository:
    ```bash
    git clone https://github.com/your-repository.git
    ```

2. Navigate to the project directory:
    ```bash
    cd your-project-directory
    ```

3. Ensure you have **JDK 21** and **Kotlin 1.9.25** installed.

4. Install the project dependencies using Gradle:
    ```bash
    ./gradlew build
    ```

5. Create Database in local Postgresql Server and update username
6. Configure your PostgreSQL database connection in `EmployeeGraphQL/src/main/kotlin/com/example/config/Config.kt`:
    ```kotlin
    @Bean
    fun createConnectionFactory(): ConnectionFactory {

        val url = "r2dbc:postgresql://localhost:5432/EmployeeGraphQL"
        val username = "zeerakzubair"

        return ConnectionFactoryBuilder
            .withUrl(url)
            .username(username)
            .build()
    }
    ```

6. Run the application:
    ```bash
    ./gradlew bootRun
    ```

## Data Model

The employee data model includes the following fields:

- `id` (Long): Unique identifier for the employee.
- `name` (String): Name of the employee.
- `age` (Int): Age of the employee.
- `class` (String): Class or department the employee belongs to.
- `subjects` (List<String>): A list of subjects taught by the employee (optional).
- `attendance` (Boolean): Attendance status of the employee.

### Example Employee Aggregate:
```kotlin
data class EmployeeAggregate (
   val id: Long? = null,
   val name: String,
   val age: Int,
   val className: String,
   val subjects: List<Subject>? = null,
   val attendance: Attendance? = null  // Nested attendance object
)
```

## GraphQL Schema

The GraphQL schema defines the structure of the API, including queries and mutations.

```graphql
type Employee {
   id: ID!
   name: String!
   age: Int!
   className: String!
   subjects: [Subject]
   attendance: Attendance
}

type Attendance {
   totalDays: Int!
   presentDays: Int!
}

type Subject {
   name: String!
}

type EmployeeConnection {
   totalCount: Int!
   edges: [EmployeeEdge!]!
   pageInfo: PageInfo!
}

type EmployeeEdge {
   cursor: String!
   node: Employee!
}

type PageInfo {
   hasNextPage: Boolean!
   hasPreviousPage: Boolean!
   startCursor: String!
   endCursor: String!
}

type Query {
   # Fetch employees using pagination
   employeesWithPagination(limit: Int, offset: Int): EmployeeConnection!

   # Fetch a list of all employees
   employees: [Employee]

   # Fetch an employee by ID
   employeeById(id: ID!): Employee

   # Fetch by age
   employeeByAge(age: Int): [Employee]

   # Fetch by name
   employeeByName(name: String): [Employee]

   # Fetch by class name
   employeeByClassName(className: String): [Employee]
}

input EmployeeInput {
   name: String!
   age: Int!
   className: String! # Matches EmployeeAggregate field
   subjects: [SubjectInput]
   attendance: AttendanceInput
}

input SubjectInput {
   name: String!
}

input AttendanceInput {
   totalDays: Int!
   presentDays: Int!
}

type Mutation {
   create(name: String, age: Int, className: String, subjects: [String], totalDays: Int, presentDays: Int): Employee
   update(id: ID, name: String!, age: Int!, className: String!): Employee
   delete(id: ID): Boolean
}
```

## GraphQL Queries

### List Employees with Optional Filters

```graphql
query GetEmployeeByName {
   employeeByName(name: "Jane Smith") {
      id
      name
      className
      subjects {
         name
      }
   }
}
```

This query returns a list of employees filtered by class, with pagination and sorting applied.

### Retrieve Details for a Single Employee

```graphql
query GetEmployeeById{
   employeeById(id: 3){
      id
      name
      age
      className
   }
}
```

This query retrieves the details of a specific employee by ID.

### List Employees with Pagination

```graphql
query GetEmployeesWithPagination {
   employeesWithPagination(limit: 5, offset: 0) {
      totalCount
      edges {
         cursor
         node {
            id
            name
            age
            className
         }
      }
      pageInfo {
         hasNextPage
         hasPreviousPage
         startCursor
         endCursor
      }
   }
}
```

This query returns a paginated list of employees.

## GraphQL Mutations

### Add Employee

```graphql
mutation CreateEmployee {
   create(
      name: "John Doe",
      age: 25,
      className: "Grade 1",
      subjects: ["Mathematics", "Science"],
      totalDays: 200,
      presentDays: 180
   ) {
      id
      name
      age
      className
      subjects{
         name
      }
      attendance {
         totalDays
         presentDays
      }
   }
}
```

This mutation adds a new employee to the system.

### Update Employee

```graphql
mutation UpdateEmployee {
   update(id: 2, name: "John Doe", age: 30, className: "Class Z") {
      id
      name
      age
      className
   }
}

```

This mutation updates the details of an existing employee by ID.

## Running the Project

Once the project is set up and the application is running, you can use any run the queries and mutations present in `EmployeeGraphQL/src/main/resources/graphql/mutations` and `EmployeeGraphQL/src/main/resources/graphql/queries`

Example:
- URL: `http://localhost:8080/graphql`
- Method: `POST`
- Body: Your GraphQL query or mutation.

---