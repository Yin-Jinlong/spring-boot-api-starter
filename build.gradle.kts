import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    java
    `maven-publish`
    signing
    alias(libs.plugins.dokka)
    alias(libs.plugins.kapt)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "io.github.yin-jinlong"
version = "0.4.0"
description = "SpringBoot Kotlin Api服务 Starter"

Props.init(rootDir)

repositories {
    projectMavenLocal(project)
    maven("https://maven.aliyun.com/repository/public")
    maven("https://maven.aliyun.com/repository/central")
    mavenCentral()
}


java {
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}


dependencies {
    kapt(libs.mica.auto)
    kapt(libs.spring.boot.configuration.processor)

    api("io.github.yin-jinlong:message-digest-kotlin:0.1.4")

    // Spring Boot + Web
    api(libs.spring.boot)
    api(libs.spring.boot.starter.web) {
        exclude("org.springframework.boot", "spring-boot-starter-json")
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    api(libs.gson)
    api(libs.spring.boot.starter.undertow)
    api(libs.spring.boot.starter.validation)
    api(libs.spring.boot.starter.websocket)

    // Database
    api(libs.mysql)
    api(libs.mybatis.spring.boot.starter)

    // Kotlin
    api(libs.kotlin.reflect)

    // Others
    api(libs.hibernate.validator)

    // Test
    testApi(libs.spring.boot.starter.test)
    testApi(libs.kotlin.test.junit5)
    testApi(libs.mybatis.spring.boot.starter.test)
    testRuntimeOnly(libs.junit.platform.launcher)
    implementation(kotlin("script-runtime"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

val pushDir = rootDir.resolve(".m2/repository/io/github/yin-jinlong/spring-boot-api-starter/$version")

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    inputs.files(tasks.named("processResources"))
}

tasks.create("kotlinJavadocjar", Jar::class) {
    group = "build"
    archiveVersion = version.toString()
    archiveClassifier = "javadoc"
    val dokka = tasks.getByName("dokkaJavadoc", DokkaTask::class)
    dependsOn("dokkaJavadoc")
    from(dokka.outputDirectory)
}


signing {
    useGpgCmd()
}


publishing {
    publications {
        create(project) {
            from(components["java"])
            val docT = tasks.getByName("kotlinJavadocjar", Jar::class)
            artifact(docT.archiveFile) {
                classifier = "javadoc"
            }

            pom {
                name = artifactId
                description = project.description
                url = "https://github.com/Yin-Jinlong/spring-boot-api-starter"

                licenses {
                    license {
                        name = "Apache 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                    }
                }

                developers {
                    developer {
                        name = "Yin Jinlong"
                        email = "yjl_1567@qq.com"
                        url = "https://github.com/Yin-Jinlong"
                        timezone = "Asia/Shanghai"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/Yin-Jinlong/spring-boot-api-starter.git"
                    developerConnection = "scm:git:ssh://github.com/Yin-Jinlong/spring-boot-api-starter.git"
                    url = "https://github.com/Yin-Jinlong/spring-boot-api-starter"
                }

            }
        }
    }

    repositories {
        projectMavenLocal(project)
    }

}


tasks.withType<PublishToMavenRepository> {
    doLast {
        pushDir.listFiles()?.forEach {
            if (it.isFile) {
                if (it.name.endsWith(".jar") ||
                    it.name.endsWith(".pom") ||
                    it.name.endsWith("module")
                ) {
                    signing.sign(it)
                }
            }
        }
    }
}