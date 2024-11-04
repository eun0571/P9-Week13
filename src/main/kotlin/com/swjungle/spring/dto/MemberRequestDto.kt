package com.swjungle.spring.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.swjungle.spring.entity.Member
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class MemberRequestDto(
    var id:Long?,

    @field:NotBlank
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{4,10}\$",
        message = "아이디는 영문, 숫자를 포함한 4~10자리로 입력해주세요"
    )
    @JsonProperty("loginId")
    private val _loginId: String?,

    @field:NotBlank
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,15}\$",
        message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~15자리로 입력해주세요"
    )
    @JsonProperty("password")
    private val _password: String?,
) {
    val loginId: String
        get() = _loginId!!

    val password: String
        get() = _password!!

    fun toEntity(): Member =
        Member(id ,loginId, password)
}

data class LoginDto(
    @field:NotBlank
    @JsonProperty("loginId")
    private val _loginId: String?,

    @field:NotBlank
    @JsonProperty("password")
    private val _password: String?,
) {
    val loginId: String
        get() = _loginId!!
    val password: String
        get() = _password!!
}