import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version Versions.kotlin
  kotlin("plugin.spring") version Versions.kotlin
  id("org.springframework.boot") version Versions.springBoot
}

apply {
  plugin("io.spring.dependency-management")
}

configure<DependencyManagementExtension> {
  imports {
    mavenBom("org.apache.camel:camel-parent:${Versions.camel}")
    mavenBom("io.zeebe:zb-bom:${Versions.zeebe}")
  }
}

dependencies {
  compile(project(":lib:camel-zeebe:camel-zeebe-api"))
  compile(project(":cloud:worker:facebam-worker-common"))
  //compile("")


  compile(kotlin("stdlib-jdk8"))
  compile(kotlin("reflect"))

}


val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  jvmTarget = Versions.java
  freeCompilerArgs = listOf("-Xjsr305=strict")
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
  jvmTarget = Versions.java
  freeCompilerArgs = listOf("-Xjsr305=strict")
}
