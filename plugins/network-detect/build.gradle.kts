import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases")
}

dependencies {
    kotlin("stable")
    compileOnly(rootProject)

    rootProject.configurations.findByName("taboo")!!.dependencies.forEach {
        compileOnly(it)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    archiveClassifier.set(null as String?)
}

tasks.build {
    dependsOn(tasks.shadowJar)
}