package com.swjungle.spring.service

import com.swjungle.spring.dto.CommentDtoResponse
import com.swjungle.spring.dto.CommentRequestDto
import com.swjungle.spring.entity.Comment
import com.swjungle.spring.entity.Member
import com.swjungle.spring.entity.Post
import com.swjungle.spring.exception.InvalidInputException
import com.swjungle.spring.repository.CommentRepository
import com.swjungle.spring.repository.MemberRepository
import com.swjungle.spring.repository.PostRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class CommentService (
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository
){
    /**
     * 댓글 작성
     */
    fun createComment(userId: Long, commentRequestDto: CommentRequestDto) :CommentDtoResponse {
        val postId = commentRequestDto.postId
        println(postId)
        val member: Member = memberRepository.findById(userId).get()
        println(member.loginId)
        val post: Post = postRepository.findById(postId).get()
        println(post.id)
        val comment: Comment = commentRequestDto.toEntity(member.loginId,post)
        commentRepository.save(comment)

        return comment.toDto()
    }

    /**
     * 댓글 수정
     */
    fun updateComment(userId: Long, commentRequestDto: CommentRequestDto) :CommentDtoResponse {
        val commentId = commentRequestDto.id
        val comment: Comment = commentRepository.findById(commentId!!).orElseThrow {
            InvalidInputException("postId", "댓글번호(${commentId})가 존재하지 않는 댓글입니다.")
        }
        val member: Member = memberRepository.findById(userId).get()
        if (!comment.checkOwner(member.loginId)) {
            throw InvalidInputException("작성자만 삭제/수정할 수 있습니다.")
        }
        comment.content = commentRequestDto.content
        commentRepository.save(comment)
        return comment.toDto()
    }

    /**
     * 댓글 삭제
     */
    fun deleteComment(userId: Long, commentId: Long) : String {
        val comment: Comment = commentRepository.findById(commentId!!).orElseThrow {
            InvalidInputException("postId", "댓글번호(${commentId})가 존재하지 않는 댓글입니다.")
        }
        val member: Member = memberRepository.findById(userId).get()
        if (!comment.checkOwner(member.loginId)) {
            throw InvalidInputException("작성자만 삭제/수정할 수 있습니다.")
        }
        commentRepository.delete(comment)
        return "댓글 삭제 성공"
    }
}