import io.papermc.paperweight.PaperweightSourceGeneratorHelper
import io.papermc.paperweight.extension.PaperweightSourceGeneratorExt

plugins {
    java
}

plugins.apply(PaperweightSourceGeneratorHelper::class)

extensions.configure(PaperweightSourceGeneratorExt::class) {
    atFile.set(projectDir.toPath().resolve("wideners.at").toFile())
}

dependencies {
    implementation("com.squareup:javapoet:1.13.0")
    implementation(project(":paper-api"))
    implementation("io.github.classgraph:classgraph:4.8.47")
    implementation("org.jetbrains:annotations:24.0.1")
}

tasks.register<JavaExec>("generate") {
    mainClass.set("io.papermc.generator.Main")
    classpath(sourceSets.main.map { it.runtimeClasspath })
    args(projectDir.toPath().resolve("generated").toString())
}

group = "io.papermc.paper"
version = "1.0-SNAPSHOT"
