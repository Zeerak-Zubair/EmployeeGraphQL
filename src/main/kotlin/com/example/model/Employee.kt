package com.example.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table

// Employee entity
@Table("employee")
data class Employee(
    @Id
    val id: Long? = null,
    val name: String,
    val age: Int,
    val className: String
){
    fun toAggregate(employeeAggregate: EmployeeAggregate): EmployeeAggregate {
        return EmployeeAggregate(
            id = this.id,
            name = this.name,
            age = this.age,
            className = this.className,
            subjects = employeeAggregate.subjects,
            attendance = employeeAggregate.attendance
        )
    }
}