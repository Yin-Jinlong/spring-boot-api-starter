package io.github.yinjinlong.spring.boot.util

import com.google.gson.*
import java.lang.reflect.Type

inline fun <reified T> GsonBuilder.registerTypeAdapter(
    crossinline serialize: (src: T?, typeOfSrc: Type?, context: JsonSerializationContext?) -> JsonElement
): GsonBuilder =
    registerTypeAdapter(T::class.java, object : JsonSerializer<T> {
        override fun serialize(src: T, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement =
            serialize(src, typeOfSrc, context)
    })


inline fun <reified T> GsonBuilder.registerTypeAdapter(
    crossinline serialize: (src: T?) -> JsonElement
): GsonBuilder = registerTypeAdapter<T> { src, _, _ ->
    serialize(src)
}

/**
 *
 * 设置排除选项
 *
 * 当判断字段时class为空
 *
 * 当判断类型时field为空
 *
 * @param shouldSkips 需要跳过的字段或类型处理函数
 */
fun GsonBuilder.setExclusionStrategies(vararg shouldSkips: (FieldAttributes?, Class<*>?) -> Boolean): GsonBuilder {
    val list = mutableListOf<ExclusionStrategy>()
    shouldSkips.forEach { shouldSkip ->
        list += object : ExclusionStrategy {

            override fun shouldSkipField(f: FieldAttributes?) = shouldSkip(f, null)

            override fun shouldSkipClass(c: Class<*>?) = shouldSkip(null, c)
        }
    }
    setExclusionStrategies(*list.toTypedArray())
    return this
}