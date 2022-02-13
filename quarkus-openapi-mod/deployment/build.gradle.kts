plugins {
    `java-library`
    `maven-publish`
}

group = "com.github.renegrob.quarkus-openapi-mod"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project



dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-core")
    implementation("io.quarkus:quarkus-core-deployment")
    implementation("io.quarkus:quarkus-smallrye-openapi-deployment")
    implementation("io.smallrye.common:smallrye-common-expression")

    implementation(project(":quarkus-openapi-mod:runtime"))
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "${project.group}"
            artifactId = "quarkus-openapi-mod"
            version = "${project.version}"

            from(components["java"])
        }
    }
}


// https://github.com/jobrunr/jobrunr/tree/master/framework-support/jobrunr-quarkus-extension
// https://github.com/quarkusio/quarkus/blob/main/integration-tests/gradle/src/main/resources/conditional-dependencies/ext-a/settings.gradle
