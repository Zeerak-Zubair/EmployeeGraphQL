package com.example.model

data class EmployeeConnection(
    val totalCount: Int,          // Total number of employees in the database
    val edges: List<EmployeeEdge>, // The paginated employees
    val pageInfo: PageInfo        // Pagination info (hasNextPage, hasPreviousPage, cursors)
)
