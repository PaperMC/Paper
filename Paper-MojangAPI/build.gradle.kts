import java.util.Locale

plugins {
    `java-library`
    `maven-publish`
}

java {
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    maven("https://libraries.minecraft.net")
}

dependencies {
    implementation(project(":Paper-API"))
    api("com.mojang:brigadier:1.0.18")

    compileOnly("it.unimi.dsi:fastutil:8.2.2")
    compileOnly("org.jetbrains:annotations:18.0.0")

    testImplementation("junit:junit:4.13.1")
    testImplementation("org.hamcrest:hamcrest-library:1.3")
    testImplementation("org.ow2.asm:asm-tree:7.3.1")
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("maven") {
        artifactId = project.name.toLowerCase(Locale.ENGLISH)
        from(components["java"])
    }
}
