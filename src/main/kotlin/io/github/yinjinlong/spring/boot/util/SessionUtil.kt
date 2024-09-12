package io.github.yinjinlong.spring.boot.util

import jakarta.servlet.http.HttpSession


operator fun HttpSession.get(s: String): Any? = getAttribute(s)

operator fun <T> HttpSession.set(s: String, v: T) = setAttribute(s, v)

operator fun HttpSession.minusAssign(s: String) = removeAttribute(s)