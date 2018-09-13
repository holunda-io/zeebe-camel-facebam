// extension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version Versions.kotlin
  id("io.spring.dependency-management") version Versions.dependencyManagement apply false
}

subprojects {

  apply {
    plugin("kotlin")
    plugin("io.spring.dependency-management")
  }

  dependencies {
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

}
