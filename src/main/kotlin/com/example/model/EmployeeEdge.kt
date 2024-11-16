package com.example.model

data class EmployeeEdge(
    val cursor: String,  // A unique cursor representing the position of this employee in the list
    val node: Employee   // The employee object itself
)