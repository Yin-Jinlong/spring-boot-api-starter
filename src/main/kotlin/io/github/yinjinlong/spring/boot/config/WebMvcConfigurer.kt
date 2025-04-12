package io.github.yinjinlong.spring.boot.config

import io.github.yinjinlong.spring.boot.exception.ExceptionResolver
import io.github.yinjinlong.spring.boot.response.JsonResponse
import io.github.yinjinlong.spring.boot.support.HandleReturnValueHandlerMethod
import io.github.yinjinlong.spring.boot.support.MessageConverter
import io.github.yinjinlong.spring.boot.support.ReturnValueHandler
import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.context.request.WebRequestInterceptor
import org.springframework.web.method.HandlerMethod
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
import kotlin.reflect.KClass

/**
 * @author YJL
 */
@Configuration
@EnableConfigurationProperties(WebMvcProperties::class)
class WebMvcConfigurer(
    val webMvcProperties: WebMvcProperties
) : WebMvcConfigurationSupport() {

    lateinit var returnValueHandler: ReturnValueHandler

    @PostConstruct
    fun init() {
        JsonResponse.registerFactory(webMvcProperties.factory)
        returnValueHandler = ReturnValueHandler(
            getBeans(MessageConverter::class)
        )
    }

    fun <T : Any> getBeans(type: KClass<T>): Collection<T> {
        return runCatching {
            applicationContext!!.getBeansOfType(type.java).values
        }.getOrDefault(emptyList())
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        runCatching {
            getBeans(HandlerInterceptor::class).forEach {
                registry.addInterceptor(it)
            }
            getBeans(WebRequestInterceptor::class).forEach {
                registry.addWebRequestInterceptor(it)
            }
        }
    }

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        val set = LinkedHashSet<HandlerMethodArgumentResolver>()
        set.addAll(argumentResolvers)
        runCatching {
            set.addAll(
                getBeans(HandlerMethodArgumentResolver::class)
            )
        }
        argumentResolvers.clear()
        argumentResolvers.addAll(set)
    }

    override fun addReturnValueHandlers(returnValueHandlers: MutableList<HandlerMethodReturnValueHandler>) {
        returnValueHandlers.add(
            returnValueHandler
        )
    }

    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        for (converter in converters) {
            when (converter) {
                is StringHttpMessageConverter -> {
                    converter.defaultCharset = Charsets.UTF_8
                }

                is MappingJackson2HttpMessageConverter -> {
                    converter.defaultCharset = Charsets.UTF_8
                }
            }
        }
    }

    override fun configureHandlerExceptionResolvers(exceptionResolvers: MutableList<HandlerExceptionResolver>) {
        exceptionResolvers.add(ExceptionResolver(this@WebMvcConfigurer.returnValueHandler))
        exceptionResolvers.add(DefaultHandlerExceptionResolver())
    }

    override fun createRequestMappingHandlerAdapter() = object : RequestMappingHandlerAdapter() {

        override fun createInvocableHandlerMethod(handlerMethod: HandlerMethod) =
            HandleReturnValueHandlerMethod(handlerMethod)

        override fun afterPropertiesSet() {
            super.afterPropertiesSet()
            returnValueHandlers = this@WebMvcConfigurer.returnValueHandlers + (this.returnValueHandlers ?: listOf())
        }
    }
}
