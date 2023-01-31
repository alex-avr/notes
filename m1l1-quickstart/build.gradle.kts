plugins {
    application
    kotlin("jvm") version "1.7.21"
}

dependencies {
    implementation(kotlin("stdlib-jdk8:1.8.0"))

    testImplementation(kotlin("test:1.8.0"))
    testImplementation(kotlin("test-junit:1.8.0"))
}

application {
    mainClass.set("org.avr.notes.m1l1.AppKt")
}
