package io.github.yinjinlong.spring.boot.exception

import io.github.yinjinlong.spring.boot.response.JsonResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @author YJL
 */
@ControllerAdvice
class ServerAdvice {

    @ExceptionHandler(Exception::class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun error(e: Exception, req: HttpServletRequest) = JsonResponse.error(e).also {
        req.inputStream.use {  }
        e.printStackTrace()
    }

}