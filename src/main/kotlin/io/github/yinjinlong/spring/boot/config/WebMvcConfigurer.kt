package io.github.yinjinlong.spring.boot.config

import io.github.yinjinlong.spring.boot.exception.ExceptionResolver
import io.github.yinjinlong.spring.boot.response.JsonResponse
import io.github.yinjinlong.spring.boot.support.MessageConverter
import io.github.yinjinlong.spring.boot.support.ReturnValueHandler
import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.context.request.WebRequestInterceptor
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
class  WebMvcConfigurer(
    val webMvcProperties: WebMvcProperties
) : WebMvcConfigurationSupport() {

    @PostConstruct
    fun init() {
        JsonResponse.registerFactory(webMvcProperties.factory)
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
            ReturnValueHandler(
                getBeans(MessageConverter::class)
            )
        )
    }

    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.clear()
        converters.add(StringHttpMessageConverter())
    }

    override fun configureHandlerExceptionResolvers(exceptionResolvers: MutableList<HandlerExceptionResolver>) {
        exceptionResolvers.add(ExceptionResolver().apply {
            customReturnValueHandlers = this@WebMvcConfigurer.returnValueHandlers.toList()
            applicationContext = this@WebMvcConfigurer.applicationContext
            afterPropertiesSet()
        })
        exceptionResolvers.add(DefaultHandlerExceptionResolver())
    }

    override fun createRequestMappingHandlerAdapter() = RequestMappingHandlerAdapter().apply {
        returnValueHandlers = this@WebMvcConfigurer.returnValueHandlers
    }
}
