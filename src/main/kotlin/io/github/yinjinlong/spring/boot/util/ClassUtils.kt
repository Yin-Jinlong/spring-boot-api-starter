package io.github.yinjinlong.spring.boot.util

import kotlin.reflect.KClass

/**
 * 是否是子类
 */
fun Class<*>.subClass(clazz: Class<*>) = isAssignableFrom(clazz)

/**
 * 是否是子类
 */
fun KClass<*>.subClass(clazz: Class<*>) = java.isAssignableFrom(clazz)
