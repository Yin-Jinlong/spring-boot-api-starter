package io.github.yinjinlong.spring.boot.support

import org.springframework.http.server.ServerHttpResponse
import java.lang.reflect.Method

/**
 * @author YJL
 */
interface MessageConverter<T : Any> {

    fun support(method: Method): Boolean = true

//    fun read(clzz: Class<out T>, inputMessage: HttpInputMessage): T?

    fun write(v: T?, method: Method, output: ServerHttpResponse)

}
