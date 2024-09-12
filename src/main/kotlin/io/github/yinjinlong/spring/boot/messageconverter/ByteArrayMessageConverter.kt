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
@ContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
class ByteArrayMessageConverter : MessageConverter<ByteArray> {

    override fun support(method: Method): Boolean {
        return method.getAnnotation(ResponseRaw::class.java) != null &&
                method.returnType == ByteArray::class.java
    }

    override fun write(v: ByteArray?, method: Method, output: ServerHttpResponse) {
        if (v == null || v.isEmpty()) {
            output.headers.contentLength = 0
            return
        }
        output.headers.contentLength = v.size.toLong()
        output.body.write(v)
    }

}