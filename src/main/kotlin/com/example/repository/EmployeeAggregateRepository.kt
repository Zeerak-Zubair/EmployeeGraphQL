package com.example.repository

import com.example.model.EmployeeAggregate
import com.example.model.EmployeeConnection


interface EmployeeAggregateRepository {
    suspend fun findAll() : List<EmployeeAggregate>
    suspend fun findById(id: Long): EmployeeAggregate
    suspend fun findByName(name: String): List<EmployeeAggregate>
    suspend fun findByAge(age: Int): List<EmployeeAggregate>
    suspend fun findByClassName(className: String): List<EmployeeAggregate>
    suspend fun create(employee: EmployeeAggregate): EmployeeAggregate
    suspend fun delete(id: Long): Boolean
    suspend fun findAllWithPagination(limit: Int, offset: Int): EmployeeConnection
    suspend fun update(id: Long, name: String, age: Int, className: String): EmployeeAggregate
}