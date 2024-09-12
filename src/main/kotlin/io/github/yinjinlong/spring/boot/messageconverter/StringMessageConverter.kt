package io.github.yinjinlong.spring.boot.messageconverter

import io.github.yinjinlong.spring.boot.annotations.ContentType
import io.github.yinjinlong.spring.boot.annotations.ResponseRaw
import io.github.yinjinlong.spring.boot.support.MessageConverter
import org.springframework.http.MediaType
import org.springframework.http.server.ServerHttpResponse
import java.lang.reflect.Method

/**
 * @author YJL
 */
@ContentType(MediaType.TEXT_PLAIN_VALUE)
class StringMessageConverter : MessageConverter<CharSequence> {

    override fun support(method: Method): Boolean {
        return method.getAnnotation(ResponseRaw::class.java) != null &&
                CharSequence::class.java.isAssignableFrom(method.returnType)
    }

    override fun write(v: CharSequence?, method: Method, output: ServerHttpResponse) {
        if (v == null) {
            output.headers.contentLength = 0
            return
        }
        output.headers.contentLength = v.length.toLong()
        output.body.write(v.toString().toByteArray())
    }

}