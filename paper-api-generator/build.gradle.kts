import org.spongepowered.gradle.vanilla.repository.MinecraftPlatform

plugins {
    java
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
}

minecraft {
    version(property("mcVersion").toString())
    platform(MinecraftPlatform.SERVER)

    runs {
        server("generate") {
            mainClass("io.papermc.generator.Main")
            accessWideners(projectDir.toPath().resolve("wideners.at"))
            args(projectDir.toPath().resolve("generated").toString())
        }
    }
}

dependencies {
    implementation("com.squareup:javapoet:1.13.0")
    implementation(project(":paper-api"))
}

group = "io.papermc.paper"
version = "1.0-SNAPSHOT"

