package com.example.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection

data class EmployeeAggregate (
    val id: Long? = null,
    val name: String,
    val age: Int,
    val className: String,
    val subjects: List<Subject>? = null,
    val attendance: Attendance? = null  // Nested attendance object
){

    private val logger = LoggerFactory.getLogger(this::class.java)
    fun toModel(): Employee {
        logger.info(this.toString())
        return Employee(
            id = this.id,
            name = this.name,
            age = this.age,
            className = this.className
        )
    }

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java)
        val selectQuery = """
        SELECT 
            e.id AS employee_id,
            e.name AS employee_name,
            e.age AS employee_age,
            e.class_name AS employee_class_name,
            s.name AS subject_name,
            a.total_days , 
            a.present_days
        FROM 
            employee e
        LEFT JOIN 
            subject s ON e.id = s.employee_id
        LEFT JOIN 
            attendance a ON e.id = a.employee_id
        """.trimIndent()

        suspend fun fromRows(rows: List<Map<String, Any>>): EmployeeAggregate{
            logger.info("Mapping Rows in Employee Aggregate")

            return withContext(Dispatchers.IO) {
                EmployeeAggregate(
                    id = rows[0]["employee_id"] as Long,
                    name = rows[0]["employee_name"] as String,
                    age = rows[0]["employee_age"] as Int,
                    className = rows[0]["employee_class_name"] as String,
                    subjects = rows.map { Subject.fromRows(it) },
                    attendance = Attendance.fromRows(rows[0])
                )
            }
        }
    }

}