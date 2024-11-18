package io.github.yinjinlong.spring.boot.annotations

import io.github.yinjinlong.spring.boot.config.WebMvcConfigurer
import org.springframework.context.annotation.Import

/**
 * @author YJL
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(WebMvcConfigurer::class)
annotation class UseWrappedReturnValue
