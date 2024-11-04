package com.swjungle.spring.dto

import java.time.LocalDateTime

data class CommentDtoResponse(
    val id: Long,
    val content: String,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime,
)
