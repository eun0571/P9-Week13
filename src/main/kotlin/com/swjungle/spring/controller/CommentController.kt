package com.swjungle.spring.controller

import com.swjungle.spring.dto.BaseResponse
import com.swjungle.spring.dto.CommentDtoResponse
import com.swjungle.spring.dto.CommentRequestDto
import com.swjungle.spring.dto.CustomUser
import com.swjungle.spring.exception.InvalidTokenException
import com.swjungle.spring.service.CommentService
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/comment")
@RestController
class CommentController (
    private val commentService: CommentService
){
    /**
     * 댓글 작성
     */
    @PostMapping
    fun createComment(@RequestBody @Valid commentRequestDto: CommentRequestDto) : CommentDtoResponse {
        val userId = try {
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        } catch (e: ClassCastException) {
            throw InvalidTokenException("잘못된 토큰입니다.") // 사용자 정의 예외 던지기
        }
        return commentService.createComment(userId, commentRequestDto)
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{comment_id}")
    fun updateComment(@RequestBody @Valid commentRequestDto: CommentRequestDto,) :CommentDtoResponse {
        val userId = try {
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        } catch (e: ClassCastException) {
            throw InvalidTokenException("잘못된 토큰입니다.") // 사용자 정의 예외 던지기
        }
        return commentService.updateComment(userId, commentRequestDto)
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{comment_id}")
    fun deleteComment(@Valid @PathVariable("comment_id") commentId : Long) : BaseResponse<String> {
        val userId = try {
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        } catch (e: ClassCastException) {
            throw InvalidTokenException("잘못된 토큰입니다.") // 사용자 정의 예외 던지기
        }
        val response = commentService.deleteComment(userId, commentId)
        return BaseResponse(msg = response)
    }
}