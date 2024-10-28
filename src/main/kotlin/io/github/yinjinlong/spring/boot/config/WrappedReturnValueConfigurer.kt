package io.github.yinjinlong.spring.boot.config

import io.github.yinjinlong.spring.boot.exception.ExceptionResolver
import io.github.yinjinlong.spring.boot.response.JsonResponse
import io.github.yinjinlong.spring.boot.support.MessageConverter
import io.github.yinjinlong.spring.boot.support.ReturnValueHandler
import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver

/**
 * @author YJL
 */
@Configuration
@EnableConfigurationProperties(TemplateProperties::class)
class WrappedReturnValueConfigurer(
    val applicationContext: ApplicationContext,
    val templateProperties: TemplateProperties
) : WebMvcConfigurationSupport() {

    @PostConstruct
    fun init() {
        JsonResponse.registerFactory(templateProperties.responseJsonFactory)
    }

    override fun addReturnValueHandlers(returnValueHandlers: MutableList<HandlerMethodReturnValueHandler>) {
        returnValueHandlers.add(
            ReturnValueHandler(
                applicationContext.getBeansOfType(MessageConverter::class.java)
                    .values
            )
        )
    }

    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.clear()
        converters.add(StringHttpMessageConverter())
    }

    override fun configureHandlerExceptionResolvers(exceptionResolvers: MutableList<HandlerExceptionResolver>) {
        exceptionResolvers.add(ExceptionResolver().apply {
            customReturnValueHandlers = this@WrappedReturnValueConfigurer.returnValueHandlers.toList()
            applicationContext = this@WrappedReturnValueConfigurer.applicationContext
            afterPropertiesSet()
        })
        exceptionResolvers.add(DefaultHandlerExceptionResolver())
    }

    override fun createRequestMappingHandlerAdapter() = RequestMappingHandlerAdapter().apply {
        returnValueHandlers = this@WrappedReturnValueConfigurer.returnValueHandlers
    }
}
