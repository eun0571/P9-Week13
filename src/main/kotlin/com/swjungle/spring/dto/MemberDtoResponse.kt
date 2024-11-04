package com.swjungle.spring.dto

import com.swjungle.spring.enums.ResultCode

data class MemberDtoResponse(
    val id: Long,
    val loginId: String,
)