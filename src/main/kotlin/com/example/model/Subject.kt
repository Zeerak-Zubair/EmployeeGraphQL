package com.example.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.relational.core.mapping.Table

@Table("subject")
data class Subject(
    val employeeId: Long? = null,
    val name: String
){
    companion object{
        suspend fun fromRows(row: Map<String, Any>): Subject{
            return withContext(Dispatchers.IO) {
                Subject(
                    employeeId = row["employee_id"] as Long,
                    name = row["subject_name"] as String
                )
            }
        }
    }
}
