package io.github.yinjinlong.spring.boot.messageconverter

import io.github.yinjinlong.spring.boot.annotations.Download
import io.github.yinjinlong.spring.boot.support.MessageConverter
import io.github.yinjinlong.spring.boot.util.subClass
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaTypeFactory
import org.springframework.http.server.ServerHttpResponse
import java.io.File
import java.lang.reflect.Method
import java.nio.file.Path

/**
 * @author YJL
 */
class FileMessageConverter : MessageConverter<Any> {

    override fun support(method: Method): Boolean {
        return File::class.subClass(method.returnType) ||
                Path::class.subClass(method.returnType)
    }

    private fun writeFile(file: File, method: Method, output: ServerHttpResponse) {
        if (!file.exists()) {
            output.setStatusCode(HttpStatus.NOT_FOUND)
            return
        }
        if (file.isDirectory) {
            output.setStatusCode(HttpStatus.FORBIDDEN)
            return
        }

        if (method.getAnnotation(Download::class.java) != null)
            output.headers.contentDisposition = ContentDisposition.attachment().filename(file.name).build()
        else {
            output.headers.contentType =
                MediaTypeFactory.getMediaType(file.name).orElse(MediaType.APPLICATION_OCTET_STREAM)
        }
        file.inputStream().copyTo(output.body)
    }

    override fun write(v: Any?, method: Method, output: ServerHttpResponse) {
        when (v) {
            null -> {
                output.setStatusCode(HttpStatus.NOT_FOUND)
                output.headers.contentLength = 0
            }

            is File -> writeFile(v, method, output)
            is Path -> writeFile(v.toFile(), method, output)
            else -> throw RuntimeException("Not supported value $v")
        }
    }

}