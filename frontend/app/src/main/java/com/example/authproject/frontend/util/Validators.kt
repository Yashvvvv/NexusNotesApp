package com.example.authproject.frontend.util
 
fun isPasswordValid(password: String): Boolean {
    val pattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}\$")
    return pattern.matches(password)
} 