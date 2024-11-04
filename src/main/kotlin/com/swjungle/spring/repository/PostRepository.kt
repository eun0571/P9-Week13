package com.swjungle.spring.repository

import com.swjungle.spring.entity.Comment
import com.swjungle.spring.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
    fun findAllByOrderByCreatedAtDesc(): List<Post>
}

interface CommentRepository : JpaRepository<Comment, Long> {
    fun findAllByOrderByCreatedAtDesc(): List<Comment>
}