package com.swjungle.spring.service

import com.swjungle.spring.dto.PostDtoResponse
import com.swjungle.spring.dto.PostRequestDto
import com.swjungle.spring.entity.Member
import com.swjungle.spring.entity.Post
import com.swjungle.spring.exception.InvalidInputException
import com.swjungle.spring.repository.PostRepository
import com.swjungle.spring.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class PostService (
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository
){
    /**
     * 전체 게시글 목록 조회
     */
    fun getPostList() : List<PostDtoResponse> {
//        val posts: List<Post> = postRepository.findAllByOrderByCreatedAtDesc()
        return postRepository.findAllByOrderByCreatedAtDesc()
            .map { it.toDto() }
    }

    /**
     * 게시글 작성
     */
    fun createPost(userId: Long, postRequestDto: PostRequestDto) : PostDtoResponse {
        val member: Member = memberRepository.findByIdOrNull(userId) ?: throw InvalidInputException("id", "회원번호(${userId})가 존재하지 않는 유저입니다.")
        val post = postRequestDto.toEntity(member.loginId)
        postRepository.save(post)

        return post.toDto()
    }

    /**
     * 게시글 조회
     */
    fun getPost(postId: Long) : PostDtoResponse {
        val post: Post = postRepository.findById(postId).orElseThrow {
            InvalidInputException("id", "게시글번호(${postId})가 존재하지 않는 게시글입니다.")
        }
        return post.toDto()
    }

    /**
     * 게시글 수정
     */
    fun updatePost(userId: Long, postRequestDto: PostRequestDto) : PostDtoResponse {
        val postId = postRequestDto.id
        val post: Post = postRepository.findById(postId!!).orElseThrow {
            InvalidInputException("postId", "게시글번호(${postId})가 존재하지 않는 게시글입니다.")
        }
        val member: Member = memberRepository.findById(userId).get()
        if (!post.checkOwner(member.loginId)) {
            throw InvalidInputException("작성자만 삭제/수정할 수 있습니다.")
        }
        post.title = postRequestDto.title
        post.content = postRequestDto.content
        postRepository.save(post)
        return post.toDto()
    }

    /**
     * 게시글 삭제
     */
    fun deletePost(userId: Long, postId: Long) : String {
        val post: Post = postRepository.findById(postId).orElseThrow {
            InvalidInputException("postId", "게시글번호(${postId})가 존재하지 않는 게시글입니다.")
        }
        val member: Member = memberRepository.findById(userId).get()
        if (!post.checkOwner(member.loginId)) {
            throw InvalidInputException("작성자만 삭제/수정할 수 있습니다.")
        }
        postRepository.delete(post)
        return "게시글 삭제 성공"
    }
}