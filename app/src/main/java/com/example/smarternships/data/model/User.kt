package com.example.smarternships.data.model

data class User(
    var userName: String = "",
    val userEmail: String  = "",
    var userDescription: String  = "",
    val userType: String = "",
    var jobs: List<String> = listOf(),
)