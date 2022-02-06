plugins {
    java
    id("io.quarkus")
}

group = "com.github.renegrob"
version = "1.0.0-SNAPSHOT"


allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-resteasy-jackson")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation(project(":myext:runtime"))
    //implementation("io.quarkus:quarkus-resteasy-qute")
    //implementation("io.quarkus:quarkus-rest-client")
    //implementation("io.quarkus:quarkus-rest-client-jackson")
    //implementation("io.quarkus:quarkus-jdbc-h2")
    //implementation("io.quarkus:quarkus-hibernate-orm")
    //implementation("io.quarkus:quarkus-spring-data-jpa")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
