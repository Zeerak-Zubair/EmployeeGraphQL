package com.example.repository

import com.example.model.*
import org.slf4j.LoggerFactory
import org.springframework.r2dbc.core.DatabaseClient
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.r2dbc.core.awaitRowsUpdated
import org.springframework.stereotype.Repository

@Repository
class EmployeeAggregateRepositoryImpl(val client: DatabaseClient, val employeeRepository: EmployeeRepository):EmployeeAggregateRepository {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun findAll(): List<EmployeeAggregate> {
        logger.info("Employee Aggregate Repository Layer - findAll()")
        val query = String.format("%s ORDER BY e.id", EmployeeAggregate.selectQuery)
        val rows = client.sql(query)
            .fetch()
            .all()
            .asFlow()
            .toList()

        //logger.info("Received rows {}",rows)
        //logger.info("Ordered rows {} by controlId",rows)
        return rows
            .groupBy { it["employee_id"] as Long }
            .map { (_, groupedRows) -> EmployeeAggregate.fromRows(groupedRows) }
    }

    override suspend fun findById(id: Long): EmployeeAggregate {
        logger.info("Employee Aggregate Repository Layer - findById()")
        val query = String.format("%s WHERE e.id = :id", EmployeeAggregate.selectQuery)
        val rows = client.sql(query)
            .bind("id", id)
            .fetch()
            .all()
            .asFlow()
            .toList()

        return EmployeeAggregate.fromRows(rows)

    }

    override suspend fun findByName(name: String): List<EmployeeAggregate> {
        logger.info("Employee Aggregate Repository Layer - findByName()")
        val query = String.format("%s WHERE e.name = :name", EmployeeAggregate.selectQuery)
        val rows = client.sql(query)
            .bind("name", name)
            .fetch()
            .all()
            .asFlow()
            .toList()

        return rows
            .groupBy { it["employee_id"] as Long }
            .map { (_, groupedRows) -> EmployeeAggregate.fromRows(groupedRows) }
    }

    override suspend fun findByAge(age: Int): List<EmployeeAggregate> {
        logger.info("Employee Aggregate Repository Layer - findByAge()")
        val query = String.format("%s WHERE e.age = :age", EmployeeAggregate.selectQuery)
        val rows = client.sql(query)
            .bind("age", age)
            .fetch()
            .all()
            .asFlow()
            .toList()

        return rows
            .groupBy { it["employee_id"] as Long }
            .map { (_, groupedRows) -> EmployeeAggregate.fromRows(groupedRows) }
    }

    override suspend fun findByClassName(className: String): List<EmployeeAggregate> {
        logger.info("Employee Aggregate Repository Layer - findByClassName()")
        val query = String.format("%s WHERE e.class_name = :className", EmployeeAggregate.selectQuery)
        val rows = client.sql(query)
            .bind("className", className)
            .fetch()
            .all()
            .asFlow()
            .toList()

        return rows
            .groupBy { it["employee_id"] as Long }
            .map { (_, groupedRows) -> EmployeeAggregate.fromRows(groupedRows) }
    }

    override suspend fun create(employee: EmployeeAggregate): EmployeeAggregate {
        logger.info("Employee Aggregate Repository Layer - findByClassName()")

        //save the employee
        val savedEmployee = saveEmployee(employee)

        //save the subjects
        val savedSubjects = saveSubjects(employeeId = savedEmployee.id!!, subjects = employee.subjects!!)

        //save the attendance
        val savedAttendance = saveAttendance(employeeId = savedEmployee.id, attendance = employee.attendance!!)

        return savedEmployee.copy(subjects = savedSubjects, attendance = savedAttendance)
    }

    private suspend fun saveEmployee(employee: EmployeeAggregate): EmployeeAggregate{
        logger.info("Employee Aggregate Repository Layer - saveEmployee()")
        val savedEmployee = employeeRepository.save(employee.toModel())
        return savedEmployee.toAggregate(savedEmployee.toAggregate(employee))
    }

    private suspend fun saveSubjects(employeeId: Long, subjects: List<Subject>): List<Subject> {
        logger.info("Employee Aggregate Repository Layer - saveSubjects()")
        val query = """
        INSERT INTO subject(employee_id, name) VALUES(:employeeId, :name)
        """.trimIndent()

        val savedSubjects = subjects.map { subject ->

            val rows = client.sql(query)
                .bind("employeeId", employeeId)
                .bind("name", subject.name)
                .fetch()
                .awaitRowsUpdated()

            subject.copy(employeeId = employeeId)
        }

        return savedSubjects
    }

    private suspend fun saveAttendance(employeeId: Long, attendance: Attendance): Attendance {
        logger.info("Employee Aggregate Repository Layer - saveAttendance()")

        val query = """
        INSERT INTO attendance(employee_id, total_days, present_days) VALUES(:employeeId, :total_days, :present_days)
        """.trimIndent()

        val savedAttendanceId = client.sql(query)
            .bind("employeeId", employeeId)
            .bind("total_days", attendance.totalDays)
            .bind("present_days", attendance.presentDays)
            .fetch()
            .awaitRowsUpdated()

        return attendance.copy(employeeId = savedAttendanceId)
    }

    override suspend fun delete(id: Long): Boolean {
        logger.info("Employee Aggregate Repository Layer - delete()")

        val query = """
            DELETE FROM employee WHERE id = :id
        """.trimIndent()

        val rowsDeleted = client.sql(query)
            .bind("id",id)
            .fetch()
            .awaitRowsUpdated()

        logger.info("Rows deleted $rowsDeleted")
        return rowsDeleted != 0L
    }

    override suspend fun findAllWithPagination(limit: Int, offset: Int): EmployeeConnection {
        logger.info("Employee Aggregate Repository Layer - findAllWithPagination()")
        val query = String.format("%s ORDER BY id LIMIT :limit OFFSET :offset", EmployeeAggregate.selectQuery)

        val rows =  client.sql(query)
            .bind("limit", limit)
            .bind("offset", offset)
            .fetch()
            .all()
            .asFlow()
            .toList()

        val employees = rows
            .groupBy { it["employee_id"] as Long }
            .map { (_, groupedRows) -> EmployeeAggregate.fromRows(groupedRows) }

        val totalCount = findAll().count() // Add a method to count total employees
        val hasNextPage = (offset + limit) < totalCount
        val hasPreviousPage = offset > 0

        val edges = employees.map { employee ->
            EmployeeEdge(
                cursor = "cursor-${employee.id}", // Create unique cursor values
                node = employee.toModel()
            )
        }

        return EmployeeConnection(
            totalCount = totalCount,
            edges = edges,
            pageInfo = PageInfo(
                hasNextPage = hasNextPage,
                hasPreviousPage = hasPreviousPage,
                startCursor = edges.firstOrNull()?.cursor.orEmpty(),
                endCursor = edges.lastOrNull()?.cursor.orEmpty()
            )
        )
    }

    override suspend fun update(id: Long, name: String, age: Int, className: String): EmployeeAggregate {
        logger.info("Employee Aggregate Repository Layer - update()")
        val existingEmployee = employeeRepository.findById(id)!!
        logger.info("Existing Employee: $existingEmployee")
        employeeRepository.save(existingEmployee.copy(name = name, age = age, className = className))

        //returning the updated employee
        return findById(id)
    }

}