plugins {
    kotlin("jvm") version "1.7.21"
}

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
    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation(kotlin("stdlib:1.8.0"))
    testImplementation(kotlin("test-junit:1.8.0"))
}


