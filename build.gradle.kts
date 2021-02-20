plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenLocal()
    mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-hibernate-orm")
    implementation("io.quarkus:quarkus-rest-client-jackson")
    implementation("io.quarkus:quarkus-jdbc-h2")
    implementation("io.quarkus:quarkus-rest-client")
    implementation("io.quarkus:quarkus-config-yaml")
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-resteasy-jackson")
    implementation("io.quarkus:quarkus-spring-data-jpa")
    implementation("io.quarkus:quarkus-resteasy-qute")
    implementation("io.quarkus:quarkus-arc")
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

tasks.register<DefaultTask>("quarkusRunConfig") {
    if (!file("${project.buildDir}/quarkus-app/quarkus-run.jar").exists()) {
        logger.lifecycle("quarkus-run.jar not found, triggering build.")
        dependsOn(tasks.getByName("build"));
    }

    doFirst {
        mkdir("${project.buildDir}/quarkus-app/config");
        file("${project.buildDir}/quarkus-app/config/application.yml").writeText(
                """
display:
  mach: 1
  unit:
    name: "km/h"
    factor: 1234.8
fix:
  value: THIS_SHOULD_NOT_BE_VISIBLE    
                     """
        )
    }
}

tasks.register<Exec>("quarkusRun") {
    dependsOn("quarkusRunConfig")
    workingDir = file("${project.buildDir}/quarkus-app");
    commandLine = listOf("java", "-jar", "quarkus-run.jar")
    standardOutput = System.out
    errorOutput = System.err
}
