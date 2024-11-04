package com.swjungle.spring.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.swjungle.spring.entity.Comment
import com.swjungle.spring.entity.Member
import com.swjungle.spring.entity.Post
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.springframework.data.annotation.CreatedBy

data class PostRequestDto(
    var id:Long?,

    @field:NotBlank
    @JsonProperty("title")
    val title: String,

    @field:NotBlank
    @JsonProperty("content")
    val content: String,

) {
    fun toEntity(createdBy: String): Post =
        Post(id, title, content, createdBy)
}

data class CommentRequestDto(
    var id:Long?,

    @field:NotBlank
    @JsonProperty("content")
    val content: String,

    @field:NotNull
    @JsonProperty("postId")
    val postId: Long,
    ) {
    fun toEntity(createdBy: String, post: Post): Comment =
        Comment(id, content, createdBy, post)
}