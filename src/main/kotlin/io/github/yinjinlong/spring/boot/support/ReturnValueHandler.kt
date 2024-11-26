package io.github.yinjinlong.spring.boot.support

import io.github.yinjinlong.spring.boot.annotations.ContentType
import io.github.yinjinlong.spring.boot.exception.BaseClientException
import io.github.yinjinlong.spring.boot.response.JsonResponse
import io.github.yinjinlong.spring.boot.util.mediaType
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.MethodParameter
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.method.support.ModelAndViewContainer
import java.lang.reflect.Method

/**
 * 返回值处理
 * @author YJL
 */
open class ReturnValueHandler(
    val converters: Collection<MessageConverter<*>>
) : HandlerMethodReturnValueHandler {

    override fun supportsReturnType(returnType: MethodParameter) = true

    private fun handleReturnValue(
        returnValue: Any?,
        method: Method,
        webRequest: NativeWebRequest
    ) {
        var converter: MessageConverter<Any>? = null
        for (c in converters) {
            if (c.support(method)) {
                converter = c as MessageConverter<Any>
                break
            }
        }

        if (converter == null)
            throw RuntimeException("Not found converter to handle $method")

        val out = ServletServerHttpResponse(
            webRequest.getNativeResponse(HttpServletResponse::class.java)
                ?: throw RuntimeException("Could not get HttpServletResponse")
        )

        val convertDefaultContentType = converter.javaClass.getAnnotation(ContentType::class.java)?.type
        val cta = method.getAnnotation(ContentType::class.java)
        out.headers.contentType = cta?.type?.mediaType ?: convertDefaultContentType?.mediaType

        converter.write(returnValue, method, out)
    }

    override fun handleReturnValue(
        returnValue: Any?,
        returnType: MethodParameter,
        mavContainer: ModelAndViewContainer,
        webRequest: NativeWebRequest
    ) {
        mavContainer.isRequestHandled = true
        handleReturnValue(
            when (returnValue) {
                is BaseClientException -> JsonResponse.clientError(returnValue)
                is Exception -> JsonResponse.error(returnValue)
                else -> returnValue
            }, returnType.method!!, webRequest
        )
    }
}
