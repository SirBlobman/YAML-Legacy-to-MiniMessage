import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Paper API
    implementation("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT") {
        exclude("net.kyori")
        exclude("it.unimi.dsi")
        exclude("net.md-5")
        exclude("org.apache.logging.log4j")
        exclude("org.apache.maven")
        exclude("org.checkerframework")
        exclude("org.joml")
        exclude("org.slf4j")
    }

    implementation("org.jetbrains:annotations:24.0.1") // JetBrains Annotations
    implementation("net.kyori:adventure-text-serializer-legacy:4.13.0") // Adventure Text Serializer: Legacy
    implementation("net.kyori:adventure-text-minimessage:4.13.0") // Adventure Text: MiniMessage
}

tasks {
    named<Jar>("jar") {
        enabled = false
    }

    named<ShadowJar>("shadowJar") {
        archiveFileName.set("converter.jar")

        minimize()
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to "xyz.sirblobman.application.yaml.legacy.minimessage.Main"
                )
            )
        }
    }
}

tasks.named("build") {
    dependsOn("shadowJar")
}
