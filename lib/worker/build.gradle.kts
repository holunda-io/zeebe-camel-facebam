import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  kotlin("plugin.spring") version Versions.kotlin
}
apply {
  plugin("io.spring.dependency-management")
}

configure<DependencyManagementExtension> {
  imports {
    mavenBom("org.apache.camel:camel-parent:${Versions.camel}")
  }
}

dependencies {
  compile(project(":lib:facebam-lib-json"))
  compile(project(":lib:camel-zeebe:camel-zeebe-api"))
  compile("org.springframework.boot:spring-boot-starter")
  compile("org.apache.camel:camel-spring-boot-starter")
  compile("org.imgscalr:imgscalr-lib:4.2")

  compile("io.github.microutils:kotlin-logging:1.6.10")

  testCompile("org.springframework.boot:spring-boot-starter-test:2.0.4.RELEASE")
  testCompile("org.assertj:assertj-core:3.10.0")
}
