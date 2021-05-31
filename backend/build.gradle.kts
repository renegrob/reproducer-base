plugins {
    `java-library`
    id("io.quarkus")
    id("org.kordamp.gradle.jandex").version("0.10.0")
}

repositories {
    mavenLocal()
    mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project
val hsqldbVersion: String by project
val infinispanVersion: String by project

dependencies {
    runtimeOnly("org.hsqldb:hsqldb:${hsqldbVersion}")

    implementation(enforcedPlatform("org.infinispan:infinispan-bom:${infinispanVersion}"))
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-config-yaml")
    api("io.quarkus:quarkus-arc")
    api("io.quarkus:quarkus-scheduler:${quarkusPlatformVersion}");
    api("io.quarkus:quarkus-hibernate-orm:${quarkusPlatformVersion}");
    api("io.quarkus:quarkus-hibernate-validator:${quarkusPlatformVersion}");
    api("io.quarkus:quarkus-spring-data-jpa")

    testImplementation("io.quarkus:quarkus-junit5")
}

group = "com.github.renegrob"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
