plugins {
    `java-library`
    `maven-publish`
}

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation(project(":paper-api"))
    api("com.mojang:brigadier:1.0.18")

    compileOnly("it.unimi.dsi:fastutil:8.5.6")
    compileOnly("org.jetbrains:annotations:23.0.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest-library:1.3")
    testImplementation("org.ow2.asm:asm-tree:9.2")
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
