package io.github.yinjinlong.spring.boot.test


import io.github.yinjinlong.spring.boot.annotations.ResponseEmpty
import io.github.yinjinlong.spring.boot.annotations.UseWrappedReturnValue
import io.github.yinjinlong.spring.boot.response.JsonResponse
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@UseWrappedReturnValue
@SpringBootApplication(scanBasePackages = ["io.github.yinjinlong.spring.boot"])
class TestApplication {
    @GetMapping("/**")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun def() = Unit

    @GetMapping("/test/void")
    fun test(@RequestParam arg: String) = Unit

    @ResponseEmpty
    @GetMapping("/test/empty")
    fun testEmpty(@RequestParam arg: String) = Unit

}

fun main(args: Array<String>) {
    runApplication<TestApplication>(*args)
}
