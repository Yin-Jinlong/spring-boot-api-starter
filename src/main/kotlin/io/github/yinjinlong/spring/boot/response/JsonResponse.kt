package io.github.yinjinlong.spring.boot.response

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

        private fun Method.checkStatic() {
            if (!Modifier.isStatic(modifiers))
                throw RuntimeException("ResponseJsonFactory.${this.name}() must be static")
        }

        private fun Method.checkReturnType() {
            if (!JsonResponse::class.subClass(returnType))
                throw RuntimeException("ResponseJsonFactory.${this.name}() return type must be JsonResponse")
        }

        private fun Method.checkArgsType(type: KClass<*>) {
            if (parameterCount != 1)
                throw RuntimeException("ResponseJsonFactory.${this.name}() args must be one")
            if (!type.subClass(parameterTypes[0]))
                throw RuntimeException("ResponseJsonFactory.${this.name}() args type must be ${type.simpleName}")
        }

        fun registerFactory(respJsonFactoryClass: Class<*>) {
            okMethod = respJsonFactoryClass.getMethod("ok", Any::class.java)
            okMethod.checkStatic()
            okMethod.checkReturnType()
            okMethod.checkArgsType(Any::class)

            errorMethod = respJsonFactoryClass.getMethod("error", Exception::class.java)
            errorMethod.checkStatic()
            errorMethod.checkReturnType()
            errorMethod.checkArgsType(Exception::class)
        }

        fun ok(data: Any?): JsonResponse {
            return okMethod.invoke(null, data) as JsonResponse
        }

        fun error(e: Exception): JsonResponse {
            return errorMethod.invoke(null, e) as JsonResponse
        }
    }
}
