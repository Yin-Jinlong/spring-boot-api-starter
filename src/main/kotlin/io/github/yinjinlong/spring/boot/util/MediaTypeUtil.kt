package io.github.yinjinlong.spring.boot.util

import org.springframework.http.MediaType

val String.mediaType: MediaType
    get() = if (!contains('/')) MediaType(this)
    else MediaType.parseMediaType(this)
