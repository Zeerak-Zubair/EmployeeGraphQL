package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EmployeeGraphQlApplication

fun main(args: Array<String>) {
	runApplication<EmployeeGraphQlApplication>(*args)
}
