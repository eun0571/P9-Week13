package com.swjungle.spring.authority

data class TokenInfo(
    val grantType: String,
    val accessToken: String,
)
