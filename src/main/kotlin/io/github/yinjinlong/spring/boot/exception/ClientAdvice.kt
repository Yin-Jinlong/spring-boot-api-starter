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
class ClientAdvice {

    @ExceptionHandler(BaseClientException::class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(e: BaseClientException, req: HttpServletRequest) = JsonResponse.clientError(e).also {
        req.inputStream.use {  }
    }
}