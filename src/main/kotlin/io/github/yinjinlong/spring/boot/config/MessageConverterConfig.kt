package io.github.yinjinlong.spring.boot.config

import com.google.gson.Gson
import io.github.yinjinlong.spring.boot.messageconverter.ByteArrayMessageConverter
import io.github.yinjinlong.spring.boot.messageconverter.FileMessageConverter
import io.github.yinjinlong.spring.boot.messageconverter.JsonMessageConverter
import io.github.yinjinlong.spring.boot.messageconverter.StringMessageConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author YJL
 */
@Configuration
class MessageConverterConfig {

    @Bean
    fun byteArrayMessageConverter() = ByteArrayMessageConverter()

    @Bean
    fun stringMessageConverter() = StringMessageConverter()

    @Bean
    fun fileMessageConverter(
        fileMediaTypeGetter: FileMessageConverter.FileMediaTypeGetter
    ) = FileMessageConverter(fileMediaTypeGetter)

    @Bean
    @Autowired
    fun jsonMessageConverter(gson: Gson) = JsonMessageConverter(gson)

    @ConditionalOnMissingBean
    @Bean
    fun defaultContentTypeParser() = FileMessageConverter.FileMediaTypeGetter.Default
}
