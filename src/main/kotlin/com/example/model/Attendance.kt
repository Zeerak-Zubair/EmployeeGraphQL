package com.example.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

// Attendance entity
@Table("attendance")
data class Attendance(
    val employeeId: Long? = null,
    val totalDays: Int,
    val presentDays: Int
){
    companion object{
        suspend fun fromRows(row: Map<String, Any>): Attendance{
            return withContext(Dispatchers.IO){
                Attendance(
                    employeeId = row["employee_id"] as Long,
                    totalDays = row["total_days"] as Int,
                    presentDays = row["present_days"] as Int
                )
            }
        }
    }
}