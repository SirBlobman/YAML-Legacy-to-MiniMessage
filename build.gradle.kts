import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("7.1.2")
}

group = "xyz.sirblobman.application"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("org.yaml:snakeyaml:1.30")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("net.kyori:adventure-text-serializer-legacy:4.11.0")
    implementation("net.kyori:adventure-text-minimessage:4.11.0")
}

tasks {
    named<Jar>("jar") {
        enabled = false
    }

    named<ShadowJar>("shadowJar") {
        manifest {
            attributes(mapOf("Main-Class" to "xyz.sirblobman.application.yaml.legacy.minimessage.Main"))
        }
        minimize()
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}
