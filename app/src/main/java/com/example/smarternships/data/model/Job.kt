package com.example.smarternships.data.model

import java.util.*

data class Job(
    var jobName: String  = "",
    val companyId: String  = "",
    var description: String  = "",
    var timeFrame: String = "",
    var assignedUserId: String  = "",
    var completed: Boolean = false,
    val applicants: List<String> = mutableListOf(),
)