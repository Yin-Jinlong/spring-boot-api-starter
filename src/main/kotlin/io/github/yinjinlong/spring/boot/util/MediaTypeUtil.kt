package io.github.yinjinlong.spring.boot.util

import org.springframework.http.MediaType

val String.mediaType: MediaType
    get() {
        if (!contains('/'))
            return MediaType(this)
        val main = this.substringBefore("/")
        val sub = this.substringAfter("/")
        return MediaType(main, sub)
    }