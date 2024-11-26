package io.github.yinjinlong.spring.boot.test

import io.github.yinjinlong.spring.boot.annotations.JsonIgnored
import io.github.yinjinlong.spring.boot.exception.BaseClientException
import io.github.yinjinlong.spring.boot.response.JsonResponse
import org.springframework.http.HttpStatus

class RespJson(
    val code: Int,
    val msg: String? = null,
    val data: Any? = null,
) : JsonResponse {
    @field:JsonIgnored
    override var status: HttpStatus = HttpStatus.OK
}

/**
 * @author YJL
 */
object RespFactory {
    @JvmStatic
    fun ok(data: Any?) = RespJson(0, data = data)

    @JvmStatic
    fun clientError(e: BaseClientException) = RespJson(e.errorCode, e.msg, e.data)

    @JvmStatic
    fun error(e: Exception): JsonResponse {
        return RespJson(1, e.message)
    }
}