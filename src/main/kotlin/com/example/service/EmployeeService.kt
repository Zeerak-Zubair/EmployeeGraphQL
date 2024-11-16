package com.example.service

import com.example.model.Attendance
import com.example.model.EmployeeAggregate
import com.example.model.EmployeeConnection
import com.example.model.Subject
import com.example.repository.EmployeeAggregateRepository
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Service

@Service
class EmployeeService(private val employeeRepository: EmployeeAggregateRepository) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    suspend fun findAll(): List<EmployeeAggregate> {
        logger.info("Employee Service Layer - indAll()")
        return employeeRepository.findAll()
    }

    suspend fun findById(id: Long): EmployeeAggregate {
        logger.info("Employee Service Layer - findById()")
        return employeeRepository.findById(id)
    }

    suspend fun findByAge(age: Int): List<EmployeeAggregate> {
        logger.info("Employee Service Layer - findByAge()")
        return employeeRepository.findByAge(age)
    }

    suspend fun findByName(name: String): List<EmployeeAggregate> {
        logger.info("Employee Service Layer - findByName()")
        return employeeRepository.findByName(name)
    }

    suspend fun findByClassName(className: String): List<EmployeeAggregate> {
        logger.info("Employee Service Layer - findByClassName()")
        return employeeRepository.findByClassName(className)
    }

    suspend fun delete(id: Long): Boolean{
        logger.info("Employee Service Layer - delete()")
        return employeeRepository.delete(id)
    }

    suspend fun create(name: String,
                       age: Int,
                       className: String,
                       subjects: List<String>,
                       totalDays: Int,
                       presentDays: Int
    ): EmployeeAggregate {

        // Map inputs to EmployeeAggregate
        val employeeAggregate = EmployeeAggregate(
            name = name,
            age = age,
            className = className,
            subjects = subjects.map { Subject(name = it) },
            attendance = Attendance(totalDays = totalDays, presentDays = presentDays)
        )
        logger.info("Employee Service Layer - create()")
        logger.info("Received employee: $employeeAggregate")
        return employeeRepository.create(employeeAggregate)
    }

    suspend fun findAllWithPagination(
        limit: Int = 10,
        offset: Int = 0
    ): EmployeeConnection {
        logger.info("Employee Service Layer - findAllWithPagination()")
        logger.info("Fetching employees with limit=$limit, offset=$offset")
        return employeeRepository.findAllWithPagination(limit, offset)
    }

    suspend fun updateEmployee(
        id: Long,
        name: String,
        age: Int,
        className: String
    ): EmployeeAggregate {
        logger.info("Employee Service Layer - updateEmployee()")
        return employeeRepository.update(id,name,age,className)
    }
}