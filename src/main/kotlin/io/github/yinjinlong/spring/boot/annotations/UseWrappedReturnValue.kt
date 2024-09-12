package io.github.yinjinlong.spring.boot.annotations

import io.github.yinjinlong.spring.boot.config.WrappedReturnValueConfigurer
import org.springframework.context.annotation.Import

/**
 * @author YJL
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(WrappedReturnValueConfigurer::class)
annotation class UseWrappedReturnValue
