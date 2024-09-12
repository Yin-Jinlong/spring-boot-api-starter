package io.github.yinjinlong.spring.boot.annotations

/**
 * 返回内容类型
 *
 * @see
 *
 * @author YJL
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContentType(
    val type: String
)
