package io.github.yinjinlong.spring.boot.exception

import io.github.yinjinlong.spring.boot.support.ReturnValueHandler
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver

/**
 * ExceptionResolver
 *
 * 重写`ReturnValueHandlers`，让`customReturnValueHandlers`处理返回值
 *
 * @author YJL
 */
class ExceptionResolver(
    val returnValueHandler: ReturnValueHandler
) : ExceptionHandlerExceptionResolver() {

    override fun afterPropertiesSet() {
        super.afterPropertiesSet()
        setReturnValueHandlers(listOf(returnValueHandler) + returnValueHandlers)
    }
}