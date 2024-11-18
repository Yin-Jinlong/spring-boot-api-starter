package io.github.yinjinlong.spring.boot.exception

import org.springframework.http.HttpStatus

/**
 * @author YJL
 */
open class BaseClientException(
    val errorCode: Int,
    val msg: String?,
    val data: Any? = null,
    val status: HttpStatus
) : RuntimeException(msg)
