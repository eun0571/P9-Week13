package com.swjungle.spring.dto

import java.time.LocalDateTime

data class PostDtoResponse (
    val id: Long,
    val title: String,
    val content: String,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime,
    val comments: List<CommentDtoResponse>
)