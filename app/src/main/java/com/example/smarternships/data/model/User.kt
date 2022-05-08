package com.example.smarternships.data.model

data class User(
    var userName: String = "",
    val userEmail: String  = "",
    var userDescription: String  = "",
    val isIntern: Boolean = true,
    var jobs: List<String> = mutableListOf(),
)