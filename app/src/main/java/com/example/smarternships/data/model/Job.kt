package com.example.smarternships.data.model

import java.util.*

data class Job(
    val jobName: String  = "",
    val companyId: String  = "",
    val description: String  = "",
    val timeFrame: Date? = null,
    val assignedUserId: String?  = null,
    val completed: Boolean = false,
    val applicants: List<String> = mutableListOf(),
)