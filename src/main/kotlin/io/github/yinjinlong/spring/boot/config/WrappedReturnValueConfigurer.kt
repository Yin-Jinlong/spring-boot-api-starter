package io.github.yinjinlong.spring.boot.config

import io.github.yinjinlong.spring.boot.response.JsonResponse
import io.github.yinjinlong.spring.boot.support.MessageConverter
import io.github.yinjinlong.spring.boot.support.ReturnValueHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodReturnValueHandler
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter

/**
 * @author YJL
 */
@Configuration
@EnableConfigurationProperties(TemplateProperties::class)
class WrappedReturnValueConfigurer : WebMvcConfigurationSupport() {

    @Autowired
    lateinit var templateProperties: TemplateProperties

    @Autowired
    lateinit var ctx: ApplicationContext

    override fun createRequestMappingHandlerAdapter() = RequestMappingHandlerAdapter().apply {
        returnValueHandlers = arrayListOf<HandlerMethodReturnValueHandler>(
            ReturnValueHandler(ctx.getBeansOfType(MessageConverter::class.java).values.toList())
        )
        JsonResponse.registerFactory(templateProperties.responseJsonFactory)
    }
}
