plugins {
    java
    id("io.quarkus")
    id("org.kordamp.gradle.jandex").version("0.7.0")
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
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-resteasy-jackson")

    implementation("io.quarkus:quarkus-scheduler:${quarkusPlatformVersion}");
    implementation("io.quarkus:quarkus-hibernate-orm:${quarkusPlatformVersion}");
    implementation("io.quarkus:quarkus-micrometer-registry-prometheus:${quarkusPlatformVersion}");
    implementation("io.quarkus:quarkus-hibernate-validator:${quarkusPlatformVersion}");
    implementation("io.quarkus:quarkus-smallrye-openapi:${quarkusPlatformVersion}");
    implementation("io.quarkus:quarkus-smallrye-health:${quarkusPlatformVersion}");
    implementation("io.quarkus:quarkus-resteasy:${quarkusPlatformVersion}");
    implementation("io.quarkus:quarkus-jackson:${quarkusPlatformVersion}");
    implementation("io.quarkus:quarkus-rest-client-jackson:${quarkusPlatformVersion}");

    implementation("io.quarkus:quarkus-spring-data-jpa")
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
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
