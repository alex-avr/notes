plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":notes-api-v1-jackson"))
    implementation(project(":notes-common"))

    testImplementation(kotlin("test-junit"))
}