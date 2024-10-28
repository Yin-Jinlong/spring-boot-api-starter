package io.github.yinjinlong.spring.boot.response

import io.github.yinjinlong.spring.boot.exception.BaseClientException
import io.github.yinjinlong.spring.boot.util.subClass
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

/**
 * Json响应
 * @author YJL
 */
interface JsonResponse {

    companion object {
        private lateinit var okMethod: Method
        private lateinit var errorMethod: Method
        private lateinit var clientErrorMethod: Method

        private fun Method.checkStatic() {
            if (!Modifier.isStatic(modifiers))
                throw RuntimeException("ResponseJsonFactory.${this.name}() must be static")
        }

        private fun Method.checkReturnType() {
            if (!JsonResponse::class.subClass(returnType))
                throw RuntimeException("ResponseJsonFactory.${this.name}() return type must be JsonResponse")
        }

        private fun Class<*>.getAndCheck(
            name: String, argClass: KClass<*>
        ): Method = getMethod(name, argClass.java).apply {
            checkStatic()
            checkReturnType()
        }

        fun registerFactory(respJsonFactoryClass: Class<*>) {
            okMethod = respJsonFactoryClass.getAndCheck("ok", Any::class)
            errorMethod = respJsonFactoryClass.getAndCheck("error", Exception::class)
            clientErrorMethod = respJsonFactoryClass.getAndCheck("clientError", BaseClientException::class)
        }

        fun ok(data: Any?): JsonResponse {
            return okMethod.invoke(null, data) as JsonResponse
        }

        fun error(e: Exception): JsonResponse {
            return errorMethod.invoke(null, e) as JsonResponse
        }

        fun clientError(e: BaseClientException): JsonResponse {
            return clientErrorMethod.invoke(null, e) as JsonResponse
        }
    }
}
