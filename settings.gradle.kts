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
include(":quarkus-openapi-mod")
include(":quarkus-openapi-mod:deployment")
include(":quarkus-openapi-mod:runtime")
include(":openapi-mod-demo")
