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
    implementation("com.aliyun:aliyun-java-sdk-core:4.6.0")
    implementation("com.aliyun:aliyun-java-sdk-alidns:2.6.32")

    rootProject.configurations.findByName("taboo")!!.dependencies.forEach {
        compileOnly(it)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    classifier = null
}

tasks.build {
    dependsOn(tasks.shadowJar)
}