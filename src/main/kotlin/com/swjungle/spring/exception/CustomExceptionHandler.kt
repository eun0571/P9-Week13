package com.swjungle.spring.exception

import com.swjungle.spring.dto.BaseResponse
import com.swjungle.spring.enums.ResultCode
import com.swjungle.spring.enums.StatusCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun methodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage ?: "Not Exception Message"
        }
        val errorMessage = errors.entries.joinToString(", ") { "${it.key}: ${it.value}" }
        return ResponseEntity(BaseResponse(StatusCode.`400`.name, errorMessage), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidInputException::class)
    protected fun invalidInputException(ex: InvalidInputException): ResponseEntity<BaseResponse<Map<String, String>>> {
//        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
//        val errorMessage = "${ex.fieldName}: ${ex.message ?: "No Exception Message"}"
        val errorMessage = ex.fieldName
        return ResponseEntity(BaseResponse(StatusCode.`400`.name, errorMessage), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BadCredentialsException::class)
    protected fun badCredentialsException(ex: BadCredentialsException): ResponseEntity<BaseResponse<Map<String, String>>> {
//        val errors = mapOf("로그인 실패" to "아이디 혹은 비밀번호를 다시 확인하세요.")
//        val errorMessage = "로그인 실패: 아이디 혹은 비밀번호를 다시 확인하세요."
        val errorMessage = "회원을 찾을 수 없습니다."

        return ResponseEntity(BaseResponse(StatusCode.`400`.name, errorMessage), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidTokenException::class)
    fun handleInvalidTokenException(ex: InvalidTokenException): ResponseEntity<BaseResponse<Nothing>> {
        return ResponseEntity(BaseResponse(StatusCode.`400`.name, ex.message), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(Exception::class)
    protected fun defaultException(ex: Exception): ResponseEntity<BaseResponse<Map<String, String>>> {
//        val errors = mapOf("미처리 에러" to (ex.message ?: "Not Exception Message"))
//        return ResponseEntity(BaseResponse(ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
        val errorMessage = "미처리 에러: ${ex.message ?: "Not Exception Message"}"
        return ResponseEntity(BaseResponse(StatusCode.`400`.name, errorMessage), HttpStatus.BAD_REQUEST)
    }

}