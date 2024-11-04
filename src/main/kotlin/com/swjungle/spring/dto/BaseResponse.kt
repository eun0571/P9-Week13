package com.swjungle.spring.dto

import com.swjungle.spring.enums.StatusCode

data class BaseResponse<T>(
    val statusCode: String = StatusCode.`200`.name,
    val msg: String? = StatusCode.`200`.msg,
)