plugins {
    kotlin("multiplatform") apply false
    kotlin("jvm") apply false
}

val JVM_TARGET = "11"

group = "org.avr.notes"
version = "0.0.1"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = JVM_TARGET
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile> {
        kotlinOptions.jvmTarget = JVM_TARGET
    }
}


