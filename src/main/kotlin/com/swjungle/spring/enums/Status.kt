package com.swjungle.spring.enums

enum class ResultCode(val msg: String) {
    SUCCESS("정상 처리 되었습니다."),
    ERROR("에러가 발생했습니다."),
}

enum class StatusCode(val msg: String) {
    `200`("정상 처리 되었습니다."),
    `400`("에러가 발생했습니다."),
}

enum class ROLE {
    MEMBER,
    ADMIN,
}