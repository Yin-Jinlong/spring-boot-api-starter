package io.github.yinjinlong.spring.boot.config

import io.github.yinjinlong.spring.boot.messageconverter.ByteArrayMessageConverter
import io.github.yinjinlong.spring.boot.messageconverter.FileMessageConverter
import io.github.yinjinlong.spring.boot.messageconverter.JsonMessageConverter
import io.github.yinjinlong.spring.boot.messageconverter.StringMessageConverter
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
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
    fun fileMessageConverter() = FileMessageConverter()

    @Bean
    @Autowired
    fun jsonMessageConverter(gson: Gson) = JsonMessageConverter(gson)

}
