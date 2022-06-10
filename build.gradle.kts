plugins {
    java
    kotlin("jvm") version "1.6.10"
    id("io.izzel.taboolib") version "1.40"
}

group = "ltd.lths.wireless.ikuai.entourage"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.tabooproject.org/repository/releases")
}

taboolib {
    install(
        "common",
        "common-5",
        "module-configuration",
        "platform-application",
    )
    classifier = null
    version = "6.0.7-56"
}

dependencies {
    kotlin("stable")

    taboo("commons-io:commons-io:2.11.0")
    taboo("org.apache.commons:commons-lang3:3.12.0")
    taboo("org.apache.httpcomponents:httpcore:4.4.15")
    taboo("org.apache.httpcomponents:httpclient:4.5.13")
    taboo("org.apache.logging.log4j:log4j-api:2.17.2")
    taboo("org.apache.logging.log4j:log4j-core:2.17.2")
    taboo("org.apache.logging.log4j:log4j-slf4j18-impl:2.17.2")

    taboo("com.google.code.gson:gson:2.9.0")

    taboo("net.minecrell:terminalconsoleappender:1.3.0")

    taboo("org.jline:jline-terminal-jansi:3.21.0")

    taboo("net.sf.jopt-simple:jopt-simple:5.0.4")

}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<Jar> {
    exclude("META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat")
    manifest {
        attributes["Main-Class"] = "${rootProject.group}.Main"
    }
}
