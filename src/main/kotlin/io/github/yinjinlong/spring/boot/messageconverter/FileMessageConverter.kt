package io.github.yinjinlong.spring.boot.messageconverter

import io.github.yinjinlong.spring.boot.support.MessageConverter
import io.github.yinjinlong.spring.boot.util.subClass
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
class FileMessageConverter(
    val fileMediaTypeGetter: FileMediaTypeGetter = FileMediaTypeGetter.Default
) : MessageConverter<Any> {

    override fun support(method: Method): Boolean {
        return File::class.subClass(method.returnType) ||
                Path::class.subClass(method.returnType)
    }

    private fun writeFile(file: File, output: ServerHttpResponse) {
        if (!file.exists()) {
            output.setStatusCode(HttpStatus.NOT_FOUND)
            return
        }
        if (file.isDirectory) {
            output.setStatusCode(HttpStatus.FORBIDDEN)
            return
        }

        output.headers.contentType = fileMediaTypeGetter.getMediaType(file)
        file.inputStream().copyTo(output.body)
    }

    override fun write(v: Any?, method: Method, output: ServerHttpResponse) {
        when (v) {
            null -> {
                output.setStatusCode(HttpStatus.NOT_FOUND)
                output.headers.contentLength = 0
            }

            is File -> writeFile(v, output)
            is Path -> writeFile(v.toFile(), output)
            else -> throw RuntimeException("Not supported value $v")
        }
    }

    interface FileMediaTypeGetter {

        companion object {
            val Default = object : FileMediaTypeGetter {
                override fun getMediaType(file: File): MediaType {
                    return MediaTypeFactory.getMediaType(file.name)
                        .orElse(MediaType.APPLICATION_OCTET_STREAM)
                }
            }
        }

        fun getMediaType(file: File): MediaType
    }

}