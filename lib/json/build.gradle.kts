import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
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
  compile("org.apache.camel:camel-jackson")
  compile("com.fasterxml.jackson.module:jackson-module-kotlin")
}


