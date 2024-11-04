package com.swjungle.spring.controller

import com.swjungle.spring.authority.TokenInfo
import com.swjungle.spring.dto.*
import com.swjungle.spring.exception.InvalidTokenException
import com.swjungle.spring.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/member")
@RestController
class MemberController (
    private val memberService: MemberService
) {
    /**
     * 회원가입
     */
    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid memberRequestDto: MemberRequestDto): BaseResponse<Any?> {
        val resultMsg: String = memberService.signUp(memberRequestDto)
        return BaseResponse(msg = resultMsg)
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    fun login(@RequestBody @Valid loginDto: LoginDto): ResponseEntity<BaseResponse<Any>> {
        val tokenInfo = memberService.login(loginDto)
        return ResponseEntity
            .ok()
            .header("Authorization", "Bearer ${tokenInfo.accessToken}")
            .body(BaseResponse(msg = "로그인 성공"))
    }

    /**
     * 내 정보 보기
     */
    @GetMapping("/info")
    fun searchMyInfo(): MemberDtoResponse {
        val userId = try {
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        } catch (e: ClassCastException) {
            throw InvalidTokenException("잘못된 토큰입니다.") // 사용자 정의 예외 던지기
        }
        return memberService.searchMyInfo(userId)
    }

    /**
     * 내 정보 수정
     */
    @PutMapping("/info")
    fun saveMyInfo(@RequestBody @Valid memberRequestDto: MemberRequestDto): BaseResponse<Any?> {
        val userId = try {
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        } catch (e: ClassCastException) {
            throw InvalidTokenException("잘못된 토큰입니다.") // 사용자 정의 예외 던지기
        }
        memberRequestDto.id = userId
        val resultMsg: String = memberService.saveMyInfo(memberRequestDto)
        return BaseResponse(msg = resultMsg)
    }

    /**
     * 내 정보 삭제
     */
    @DeleteMapping("/info")
    fun deleteMyInfo(): BaseResponse<Any?> {
        val userId = try {
            (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        } catch (e: ClassCastException) {
            throw InvalidTokenException("잘못된 토큰입니다.") // 사용자 정의 예외 던지기
        }
        val response = memberService.deleteMyInfo(userId)
        return BaseResponse(msg = response)
    }
}