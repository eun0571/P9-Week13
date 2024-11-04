package com.swjungle.spring.controller

import com.swjungle.spring.dto.*
import com.swjungle.spring.entity.Post
import com.swjungle.spring.exception.InvalidTokenException
import com.swjungle.spring.service.PostService
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/post")
@RestController
class PostController (
    private val postService: PostService
){
    /**
     * 전체 게시글 조회
     */
    @GetMapping("/")
    fun getPostList() : Map<String, List<PostDtoResponse>> {
        return mapOf("postList" to postService.getPostList())
    }

    /**
     * 게시글 작성
     */
    @PostMapping("/")
    fun createPost(@RequestBody @Valid postRequestDto: PostRequestDto) : PostDtoResponse {
        val userId = try {
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        } catch (e: ClassCastException) {
            throw InvalidTokenException("잘못된 토큰입니다.") // 사용자 정의 예외 던지기
        }
        return postService.createPost(userId, postRequestDto)
    }

    /**
     * 게시글 조회
     */
    @GetMapping("/{post_id}")
    fun getPost(@Valid @PathVariable("post_id") postId: Long) : PostDtoResponse {
        return postService.getPost(postId)
    }

    /**
     * 게시글 수정
     */
    @PutMapping("/{post_id}")
    fun updatePost(@RequestBody @Valid postRequestDto: PostRequestDto) : PostDtoResponse {
        val userId = try {
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        } catch (e: ClassCastException) {
            throw InvalidTokenException("잘못된 토큰입니다.") // 사용자 정의 예외 던지기
        }
        return postService.updatePost(userId, postRequestDto)
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/{post_id}")
    fun deletePost(@Valid @PathVariable("post_id") postId: Long) : BaseResponse<String> {
        val userId = try {
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        } catch (e: ClassCastException) {
            throw InvalidTokenException("잘못된 토큰입니다.") // 사용자 정의 예외 던지기
        }
        val response = postService.deletePost(userId, postId)
        return BaseResponse(msg = response)
    }
}