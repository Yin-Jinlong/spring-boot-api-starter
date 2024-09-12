package io.github.yinjinlong.spring.boot.exception

/**
 * @author YJL
 */
open class BaseClientException(
    val errorCode: Int,
    val msg: String?,
    val data: Any? = null
) : RuntimeException(msg)
