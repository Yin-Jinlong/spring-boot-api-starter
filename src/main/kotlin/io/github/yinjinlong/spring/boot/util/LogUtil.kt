package io.github.yinjinlong.spring.boot.util

import java.util.logging.Logger

fun Any.getLogger() = Logger.getLogger(this.javaClass.toGenericString())
