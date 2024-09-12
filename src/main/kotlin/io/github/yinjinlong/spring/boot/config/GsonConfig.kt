package io.github.yinjinlong.spring.boot.config

import io.github.yinjinlong.spring.boot.annotations.JsonIgnored
import io.github.yinjinlong.spring.boot.util.registerTypeAdapter
import io.github.yinjinlong.spring.boot.util.setExclusionStrategies
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.Timestamp

/**
 * @author YJL
 */
@Configuration
class GsonConfig {
    @Bean
    fun globalGsonBuilder() = GsonBuilder().setExclusionStrategies({ f, c ->
        if (f != null) {
            f.getAnnotation(JsonIgnored::class.java) != null
        } else if (c != null)
            c.getAnnotation(JsonIgnored::class.java) != null
        else
            false
    }).registerTypeAdapter<Timestamp> { src ->
        JsonPrimitive(src?.time)
    }

    @Bean
    fun globalGson(gb: GsonBuilder): Gson = gb.create()
}
