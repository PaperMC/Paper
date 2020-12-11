plugins {
    `java-library`
    `maven-publish`
}

java {
    withSourcesJar()
    withJavadocJar()
}

val annotationsVersion = "24.1.0"
val bungeeCordChatVersion = "1.20-R0.2"

dependencies {
    // api dependencies are listed transitively to API consumers
    api("com.google.guava:guava:33.3.1-jre")
    api("com.google.code.gson:gson:2.11.0")
    api("net.md-5:bungeecord-chat:$bungeeCordChatVersion")
    api("org.yaml:snakeyaml:2.2")
    api("org.joml:joml:1.10.8") {
        isTransitive = false // https://github.com/JOML-CI/JOML/issues/352
    }

    compileOnly("org.apache.maven:maven-resolver-provider:3.9.6")
    compileOnly("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.18")
    compileOnly("org.apache.maven.resolver:maven-resolver-transport-http:1.9.18")

    val annotations = "org.jetbrains:annotations-java5:$annotationsVersion"
    compileOnly(annotations)
    testCompileOnly(annotations)

    testImplementation("org.apache.commons:commons-lang3:3.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.mockito:mockito-core:5.14.1")
    testImplementation("org.ow2.asm:asm-tree:9.7.1")
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

val generateApiVersioningFile by tasks.registering {
    inputs.property("version", project.version)
    val pomProps = layout.buildDirectory.file("pom.properties")
    outputs.file(pomProps)
    val projectVersion = project.version
    doLast {
        pomProps.get().asFile.writeText("version=$projectVersion")
    }
}

tasks.jar {
    from(generateApiVersioningFile.map { it.outputs.files.singleFile }) {
        into("META-INF/maven/${project.group}/${project.name}")
    }
    manifest {
        attributes(
            "Automatic-Module-Name" to "org.bukkit"
        )
    }
}

tasks.withType<Javadoc> {
    val options = options as StandardJavadocDocletOptions
    options.overview = "src/main/javadoc/overview.html"
    options.use()
    options.isDocFilesSubDirs = true
    options.links(
        "https://guava.dev/releases/33.3.1-jre/api/docs/",
        "https://javadoc.io/doc/org.yaml/snakeyaml/2.2/",
        "https://javadoc.io/doc/org.jetbrains/annotations-java5/$annotationsVersion/",
        "https://javadoc.io/doc/net.md-5/bungeecord-chat/$bungeeCordChatVersion/",
    )
    options.tags("apiNote:a:API Note:")

    // workaround for https://github.com/gradle/gradle/issues/4046
    inputs.dir("src/main/javadoc").withPropertyName("javadoc-sourceset")
    doLast {
        copy {
            from("src/main/javadoc") {
                include("**/doc-files/**")
            }
            into("build/docs/javadoc")
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
