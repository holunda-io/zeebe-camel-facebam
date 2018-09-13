// extension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
}

subprojects {

  apply {
    plugin("kotlin")
  }

  dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = "1.8"
      //  freeCompilerArgs = kotlin.collections.listOf("-Xjsr305=strict")
    }
  }


}
