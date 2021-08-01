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

    compileOnly("it.unimi.dsi:fastutil:8.5.4")
    compileOnly("org.jetbrains:annotations:21.0.1")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
    testImplementation("org.ow2.asm:asm-tree:9.2")
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("maven") {
        artifactId = project.name.toLowerCase(Locale.ENGLISH)
        from(components["java"])
    }
}
