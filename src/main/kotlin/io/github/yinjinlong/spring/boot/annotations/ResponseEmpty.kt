package io.github.yinjinlong.spring.boot.annotations

/**
 * 使用该注解会直接返回，没有body内容
 * @author YJL
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ResponseEmpty
