// extension
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version Versions.kotlin
  kotlin("plugin.spring") version Versions.kotlin
  id("org.springframework.boot") version Versions.springBoot apply false
}

subprojects {

  apply {
    plugin("kotlin")
    plugin("org.springframework.boot")
  }

  dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))

    compile(project(":lib:facebam-lib-worker"))
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

}
