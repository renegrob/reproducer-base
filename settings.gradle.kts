pluginManagement {
    val quarkusPluginVersion: String by settings
    val quarkusPluginId: String by settings
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id(quarkusPluginId) version quarkusPluginVersion
        id("io.quarkus.extension") version quarkusPluginVersion
    }
}
rootProject.name="reproducer-base"
include(":myext")
include(":myext:deployment")
include(":myext:runtime")
