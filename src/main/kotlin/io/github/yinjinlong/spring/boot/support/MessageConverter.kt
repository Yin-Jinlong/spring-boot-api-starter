package io.github.yinjinlong.spring.boot.support

import org.springframework.http.MediaType
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import java.lang.reflect.Method

/**
 * @author YJL
 */
interface MessageConverter<T : Any> {

    /**
     * 是否支持（写）
     */
    fun support(method: Method): Boolean = true

    /**
     * 是否支持（读）
     */
    fun support(mediaType: MediaType): Boolean = false

    fun read(input: ServerHttpRequest): T? = null

    fun write(v: T?, method: Method, output: ServerHttpResponse)

}
