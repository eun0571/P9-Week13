package com.swjungle.spring.service

import com.swjungle.spring.authority.JwtTokenProvider
import com.swjungle.spring.authority.TokenInfo
import com.swjungle.spring.dto.LoginDto
import com.swjungle.spring.dto.MemberDtoResponse
import com.swjungle.spring.dto.MemberRequestDto
import com.swjungle.spring.entity.Member
import com.swjungle.spring.entity.MemberRole
import com.swjungle.spring.enums.ROLE
import com.swjungle.spring.exception.InvalidInputException
import com.swjungle.spring.repository.MemberRepository
import com.swjungle.spring.repository.MemberRoleRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
){
    /**
     * 회원가입
     */
    fun signUp(memberRequestDto: MemberRequestDto) : String {
        // ID 중복 검사
        var member: Member? = memberRepository.findByLoginId(memberRequestDto.loginId)
        if (member != null){
            throw InvalidInputException("loginId", "이미 등록된 ID 입니다.")
        }

        member = memberRequestDto.toEntity()
        memberRepository.save(member)

        val memberRole: MemberRole = MemberRole(null, ROLE.MEMBER, member)
        memberRoleRepository.save(memberRole)

        return "회원가입 성공"
    }

    /**
     * 로그인 -> 토큰 발행
     */
    fun login(loginDto: LoginDto): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.loginId, loginDto.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        return jwtTokenProvider.createToken(authentication)
    }

    /**
     * 내 정보 조회
     */
    fun searchMyInfo(id: Long): MemberDtoResponse {
        val member: Member = memberRepository.findByIdOrNull(id) ?: throw InvalidInputException("id", "회원번호(${id})가 존재하지 않는 유저입니다.")
        return member.toDto()
    }

    /**
     * 내 정보 수정
     */
    fun saveMyInfo(memberRequestDto: MemberRequestDto): String {
        val member = memberRequestDto.toEntity()
        memberRepository.save(member)
        return "수정 완료되었습니다."
    }

    /**
     * 내 정보 삭제
     */
    fun deleteMyInfo(id: Long): String {
        val member: Member = memberRepository.findByIdOrNull(id) ?: throw InvalidInputException("id", "회원번호(${id})가 존재하지 않는 유저입니다.")
        memberRoleRepository.deleteAllByMember(member)
        memberRepository.delete(member)
        return "삭제 완료되었습니다."
    }
}