package io.github.yinjinlong.spring.boot.messageconverter

import io.github.yinjinlong.spring.boot.annotations.ContentType
import io.github.yinjinlong.spring.boot.support.MessageConverter
import io.github.yinjinlong.spring.boot.util.subClass
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.ServerHttpResponse
import java.io.File
import java.lang.reflect.Method
import java.nio.file.NoSuchFileException
import java.nio.file.Path

/**
 * @author YJL
 */
@ContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE)
class FileMessageConverter : MessageConverter<Any> {

    override fun support(method: Method): Boolean {
        return File::class.subClass(method.returnType)
    }

    private fun writeFile(file: File, output: ServerHttpResponse) {
        if (!file.exists()) {
            throw NoSuchFileException(file.absolutePath)
        }
        if (file.isDirectory)
            throw IllegalArgumentException("Not a file: ${file.absolutePath}")

        file.inputStream().use {
            it.copyTo(output.body)
        }
    }

    override fun write(v: Any?, method: Method, output: ServerHttpResponse) {
        when (v) {
            null -> {
                throw NoSuchFileException("null")
            }

            is File -> writeFile(v, output)
            else -> throw RuntimeException("Not supported value $v")
        }
    }
}