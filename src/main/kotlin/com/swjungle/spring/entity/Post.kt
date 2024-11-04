package com.swjungle.spring.entity

import com.swjungle.spring.dto.CommentDtoResponse
import com.swjungle.spring.dto.MemberDtoResponse
import com.swjungle.spring.dto.PostDtoResponse
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy

@Entity
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Column(nullable = false, length = 255)
    var title: String,

    @Column(nullable = false, length = 6553)
    var content: String,

    @Column(nullable = false, updatable = false, length = 30)
    val createdBy: String,

    ) : Timestamped() {  // Timestamped를 상속
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = [(CascadeType.ALL)])
    val comments: MutableList<Comment> = mutableListOf()

    fun checkOwner(loginId: String): Boolean = this.createdBy == loginId

    fun toDto(): PostDtoResponse =
        PostDtoResponse(id!!, title, content, createdBy, createdAt!!, modifiedAt!!, comments.map { it.toDto() })
}

@Entity
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Column(nullable = false, length = 6553)
    var content: String,

    @Column(nullable = false, updatable = false, length = 30)
    val createdBy: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_comment_post_id"))
    val post: Post,
) : Timestamped() {  // Timestamped를 상속

    fun checkOwner(loginId: String): Boolean = this.createdBy == loginId

    fun toDto(): CommentDtoResponse =
        CommentDtoResponse(id!!, content, createdBy, createdAt!!, modifiedAt!!)
}