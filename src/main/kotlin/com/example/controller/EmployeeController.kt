package com.example.controller

import com.example.model.*
import com.example.repository.EmployeeAggregateRepository
import com.example.service.EmployeeService
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class EmployeeController(val employeeService: EmployeeService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @SchemaMapping(typeName = "Query",value = "employees")
    suspend fun findAll(): List<EmployeeAggregate> {
        logger.info("Employee Controller layer - findAll()")
        return employeeService.findAll()
    }

    @SchemaMapping(typeName = "Query", value = "employeeById")
    suspend fun findById(@Argument id: Long): EmployeeAggregate{
        logger.info("Employee Controller layer - findById()")
        return employeeService.findById(id)
    }

    @SchemaMapping(typeName = "Query", value = "employeeByAge")
    suspend fun findByAge(@Argument age: Int): List<EmployeeAggregate> {
        logger.info("Employee Controller layer - findByAge()")
        return employeeService.findByAge(age)
    }

    @SchemaMapping(typeName = "Query", value = "employeeByName")
    suspend fun findByName(@Argument name: String): List<EmployeeAggregate> {
        logger.info("Employee Controller layer - findByName()")
        return employeeService.findByName(name)
    }

    @SchemaMapping(typeName = "Query", value = "employeeByClassName")
    suspend fun findByClassName(@Argument className: String): List<EmployeeAggregate> {
        logger.info("Employee Controller layer - findByClassName()")
        return employeeService.findByClassName(className)
    }

    @SchemaMapping(typeName = "Mutation", value = "delete")
    suspend fun delete(@Argument id: Long): Boolean{
        logger.info("Employee Controller layer - delete()")
        return employeeService.delete(id)
    }

    @SchemaMapping(typeName = "Mutation", value = "create")
    suspend fun create(@Argument name: String,
                       @Argument age: Int,
                       @Argument className: String,
                       @Argument subjects: List<String>,
                       @Argument totalDays: Int,
                       @Argument presentDays: Int
    ): EmployeeAggregate {
        logger.info("Employee Controller layer - create()")
        return employeeService.create(name, age, className, subjects, totalDays, presentDays)
    }

    @SchemaMapping(typeName = "Query", field = "employeesWithPagination")
    suspend fun employees(
        @Argument limit: Int = 10,
        @Argument offset: Int = 0
    ): EmployeeConnection {
        logger.info("Employee Controller layer - employees()")
        logger.info("Fetching employees with limit=$limit, offset=$offset")
        return employeeService.findAllWithPagination(limit, offset)
    }

    @SchemaMapping(typeName = "Mutation", field = "update")
    suspend fun updateEmployee(
        @Argument id: Long,
        @Argument name: String,
        @Argument age: Int,
        @Argument className: String
    ): EmployeeAggregate {
        logger.info("Employee Controller layer - updateEmployee()")
        // Update logic, e.g., calling service to update employee
        return employeeService.updateEmployee(id, name, age, className)
    }


}