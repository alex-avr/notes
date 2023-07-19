plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}
    linuxX64 {}
    macosX64()

    sourceSets {
        val coroutinesVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(project(":notes-common"))
                implementation(project(":notes-stubs"))

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(project(":notes-repo-tests"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
