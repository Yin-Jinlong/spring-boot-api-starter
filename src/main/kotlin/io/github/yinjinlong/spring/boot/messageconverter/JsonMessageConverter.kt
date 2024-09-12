package io.github.yinjinlong.spring.boot.messageconverter

import io.github.yinjinlong.spring.boot.annotations.ContentType
import io.github.yinjinlong.spring.boot.response.JsonResponse
import io.github.yinjinlong.spring.boot.support.MessageConverter
import com.google.gson.Gson
import org.springframework.http.MediaType
import org.springframework.http.server.ServerHttpResponse
import java.lang.reflect.Method

/**
 * @author YJL
 */
@ContentType(MediaType.APPLICATION_JSON_VALUE)
class JsonMessageConverter(
    val gson: Gson
) : MessageConverter<Any> {

    override fun write(v: Any?, method: Method, output: ServerHttpResponse) {
        output.body.write(
            gson.toJson(
                if (v is JsonResponse) v else JsonResponse.ok(v)
            ).toByteArray()
        )
    }

}