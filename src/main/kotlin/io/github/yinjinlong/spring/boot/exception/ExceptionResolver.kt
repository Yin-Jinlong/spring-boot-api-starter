package io.github.yinjinlong.spring.boot.exception

import io.github.yinjinlong.spring.boot.support.ReturnValueHandler
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
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
    override fun getDefaultReturnValueHandlers(): MutableList<HandlerMethodReturnValueHandler> {
        return mutableListOf<HandlerMethodReturnValueHandler>().apply {
            // Custom return value types
            customReturnValueHandlers?.let {
                addAll(it)
            }
            if (isEmpty())
                addAll(super.getDefaultReturnValueHandlers())
        }
    }

    override fun afterPropertiesSet() {
        super.afterPropertiesSet()
        customReturnValueHandlers = listOf(returnValueHandler) + (this.customReturnValueHandlers ?: listOf())
    }
}