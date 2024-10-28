package io.github.yinjinlong.spring.boot.test

import io.github.yinjinlong.spring.boot.response.JsonResponse

class RespJson : JsonResponse {

}

/**
 * @author YJL
 */
object RespFactory {
    @JvmStatic
    fun ok(data: Any?) = RespJson()

    @JvmStatic
    fun error(e: Exception): JsonResponse {
        return RespJson()
    }
}