package com.swjungle.spring.exception

class InvalidInputException(
    val fieldName: String = "",
    message: String = "Invalid input"
) : RuntimeException(message)