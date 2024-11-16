package com.example.repository

import com.example.model.Employee
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : CoroutineCrudRepository<Employee, Long>