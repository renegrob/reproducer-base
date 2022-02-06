plugins {
    `java-library`
    `maven-publish`
}

group = "com.github.renegrob.myext"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-core-deployment")
    implementation("io.quarkus:quarkus-smallrye-openapi-deployment")
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation(project(":myext:runtime"))
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "${project.group}"
            artifactId = "myext"
            version = "${project.version}"

            from(components["java"])
        }
    }
}


// https://github.com/jobrunr/jobrunr/tree/master/framework-support/jobrunr-quarkus-extension
// https://github.com/quarkusio/quarkus/blob/main/integration-tests/gradle/src/main/resources/conditional-dependencies/ext-a/settings.gradle
