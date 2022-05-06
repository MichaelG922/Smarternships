package com.example.smarternships.data.model

data class User(
    val userName: String  = "",
    val userEmail: String  = "",
    val userDescription: String  = "",
    val isIntern: Boolean = true,
    val currentJobs: List<Job> = mutableListOf(),
    val completedJobs: List<Job> = mutableListOf(),
)