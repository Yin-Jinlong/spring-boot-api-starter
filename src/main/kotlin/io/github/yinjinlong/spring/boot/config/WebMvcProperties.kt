package io.github.yinjinlong.spring.boot.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author YJL
 */
@ConfigurationProperties(prefix = "server.resp")
data class WebMvcProperties(
    /**
     * json包装工厂
     *
     * @see io.github.yinjinlong.spring.boot.response.JsonResponse.registerFactory
     */
    val factory: Class<*>,
)
