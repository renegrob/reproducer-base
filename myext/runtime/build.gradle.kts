plugins {
    java
    id("io.quarkus.extension")
}

group = "com.github.renegrob.myext"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

quarkusExtension {
    //deploymentArtifact = "${project.group}:myext:${project.version}"
    //deploymentArtifact = ":myext-deployment"
    //dependencyConditions = listOf("io.quarkus:quarkus-smallrye-openapi")
}

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
}


// https://github.com/jobrunr/jobrunr/tree/master/framework-support/jobrunr-quarkus-extension
// https://github.com/quarkusio/quarkus/blob/main/integration-tests/gradle/src/main/resources/conditional-dependencies/ext-a/settings.gradle
