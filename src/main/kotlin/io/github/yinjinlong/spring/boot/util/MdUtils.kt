@file:Suppress("unused")

package io.github.yinjinlong.spring.boot.util

import io.github.yinjinlong.spring.boot.security.MD
import java.security.MessageDigest

fun MessageDigest.digest(data: CharSequence) =
    digest(data.toString().toByteArray()).joinToString {
        Integer.toHexString(it.toInt())
    }

val CharSequence.md2: String
    get() = MD.md2.digest(this)

val CharSequence.md5: String
    get() = MD.md5.digest(this)

val CharSequence.sha1: String
    get() = MD.sha1.digest(this)

val CharSequence.sha224: String
    get() = MD.sha224.digest(this)

val CharSequence.sha256: String
    get() = MD.sha256.digest(this)

val CharSequence.sha384: String
    get() = MD.sha384.digest(this)

val CharSequence.sha512: String
    get() = MD.sha512.digest(this)

val CharSequence.sha512_224: String
    get() = MD.sha512_224.digest(this)

val CharSequence.sha512_256: String
    get() = MD.sha512_256.digest(this)

val CharSequence.sha3_224: String
    get() = MD.sha3_224.digest(this)

val CharSequence.sha3_256: String
    get() = MD.sha3_256.digest(this)

val CharSequence.sha3_384: String
    get() = MD.sha3_384.digest(this)

val CharSequence.sha3_512: String
    get() = MD.sha3_512.digest(this)
