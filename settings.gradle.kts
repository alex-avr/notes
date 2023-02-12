
rootProject.name = "notes"

include("m1l1-quickstart")

pluginManagement {
    plugins {
        val kotlinVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
    }
}