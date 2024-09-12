package io.github.yinjinlong.spring.boot.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author YJL
 */
@ConfigurationProperties(prefix = "spring")
data class TemplateProperties(
    val responseJsonFactory: Class<*>
)