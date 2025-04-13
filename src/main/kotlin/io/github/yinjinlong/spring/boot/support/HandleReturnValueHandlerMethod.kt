package io.github.yinjinlong.spring.boot.support

import io.github.yinjinlong.spring.boot.annotations.ResponseEmpty
import io.github.yinjinlong.spring.boot.annotations.SkipHandle
import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.util.StringUtils
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.method.HandlerMethod
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.View
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod
import java.io.IOException

/**
 * @author YJL
 */
class HandleReturnValueHandlerMethod(
    handlerMethod: HandlerMethod
) : ServletInvocableHandlerMethod(handlerMethod) {

    var returnValueHandlers: HandlerMethodReturnValueHandlerComposite? = null

    override fun setHandlerMethodReturnValueHandlers(returnValueHandlers: HandlerMethodReturnValueHandlerComposite) {
        super.setHandlerMethodReturnValueHandlers(returnValueHandlers)
        this.returnValueHandlers = returnValueHandlers
    }

    // copy from super
    @Throws(IOException::class)
    private fun setResponseStatus(webRequest: ServletWebRequest) {
        val status = responseStatus ?: return

        val response = webRequest.response
        if (response != null) {
            val reason = responseStatusReason
            if (StringUtils.hasText(reason)) {
                response.sendError(status.value(), reason)
            } else {
                response.status = status.value()
            }
        }

        // To be picked up by RedirectView
        webRequest.request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, status)
    }

    override fun invokeAndHandle(
        webRequest: ServletWebRequest,
        mavContainer: ModelAndViewContainer,
        vararg providedArgs: Any?
    ) {
        if (AnnotatedElementUtils.hasAnnotation(resolvedFromHandlerMethod!!.method, SkipHandle::class.java))
            return super.invokeAndHandle(webRequest, mavContainer, *providedArgs)

        val returnValue = invokeForRequest(webRequest, mavContainer, *providedArgs)
        setResponseStatus(webRequest)

        if (returnValue == null) {
            if (resolvedFromHandlerMethod?.hasMethodAnnotation(ResponseEmpty::class.java) == true) {
                mavContainer.isRequestHandled = true
                return
            }
        } else if (StringUtils.hasText(responseStatusReason)) {
            mavContainer.isRequestHandled = true
            return
        }

        mavContainer.isRequestHandled = false
        check(returnValueHandlers != null) { "no return value handler" }
        returnValueHandlers!!.handleReturnValue(
            returnValue,
            getReturnValueType(returnValue),
            mavContainer,
            webRequest
        )
    }
}
