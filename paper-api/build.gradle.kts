plugins {
    `java-library`
    `maven-publish`
    idea // Paper
}

java {
    withSourcesJar()
    withJavadocJar()
}

val annotationsVersion = "24.1.0"
val bungeeCordChatVersion = "1.20-R0.2"

// Paper start - configure mockito agent that is needed in newer java versions
val mockitoAgent = configurations.register("mockitoAgent")
abstract class MockitoAgentProvider : CommandLineArgumentProvider {
    @get:CompileClasspath
    abstract val fileCollection: ConfigurableFileCollection

    override fun asArguments(): Iterable<String> {
        return listOf("-javaagent:" + fileCollection.files.single().absolutePath)
    }
}
// Paper end - configure mockito agent that is needed in newer java versions

dependencies {
    // api dependencies are listed transitively to API consumers
    api("com.google.guava:guava:33.3.1-jre")
    api("com.google.code.gson:gson:2.11.0")
    api("net.md-5:bungeecord-chat:$bungeeCordChatVersion")
    api("org.yaml:snakeyaml:2.2")
    api("org.joml:joml:1.10.8") {
        isTransitive = false // https://github.com/JOML-CI/JOML/issues/352
    }
    // Paper start
    api("com.googlecode.json-simple:json-simple:1.1.1") {
        isTransitive = false // includes junit
    }
    // Paper end

    compileOnly("org.apache.maven:maven-resolver-provider:3.9.6")
    compileOnly("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.18")
    compileOnly("org.apache.maven.resolver:maven-resolver-transport-http:1.9.18")

    val annotations = "org.jetbrains:annotations:$annotationsVersion" // Paper - we don't want Java 5 annotations...
    compileOnly(annotations)
    testCompileOnly(annotations)

    // Paper start - add checker
    val checkerQual = "org.checkerframework:checker-qual:3.33.0"
    compileOnlyApi(checkerQual)
    testCompileOnly(checkerQual)
    // Paper end
    api("org.jspecify:jspecify:1.0.0") // Paper - add jspecify

    testImplementation("org.apache.commons:commons-lang3:3.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.mockito:mockito-core:5.14.1")
    testImplementation("org.ow2.asm:asm-tree:9.7.1")
    mockitoAgent("org.mockito:mockito-core:5.14.1") { isTransitive = false } // Paper - configure mockito agent that is needed in newer java versions
}

// Paper start
val generatedApiPath: java.nio.file.Path = rootProject.projectDir.toPath().resolve("paper-api-generator/generated")
idea {
    module {
        generatedSourceDirs.add(generatedApiPath.toFile())
    }
}
sourceSets {
    main {
        java {
            srcDir(generatedApiPath)
        }
    }
}
// Paper end

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
        "https://javadoc.io/doc/org.jetbrains/annotations/$annotationsVersion/", // Paper - we don't want Java 5 annotations
        "https://javadoc.io/doc/net.md-5/bungeecord-chat/$bungeeCordChatVersion/",
        // Paper start - add missing javadoc links
        "https://javadoc.io/doc/org.joml/joml/1.10.8/index.html",
        "https://www.javadoc.io/doc/com.google.code.gson/gson/2.11.0",
        "https://jspecify.dev/docs/api/",
        // Paper end
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
    // Paper start - configure mockito agent that is needed in newer java versions
    val provider = objects.newInstance<MockitoAgentProvider>()
    provider.fileCollection.from(mockitoAgent)
    jvmArgumentProviders.add(provider)
    // Paper end - configure mockito agent that is needed in newer java versions
}

// Paper start - compile tests with -parameters for better junit parameterized test names
tasks.compileTestJava {
    options.compilerArgs.add("-parameters")
}
// Paper end

// Paper start
val scanJar = tasks.register("scanJarForBadCalls", io.papermc.paperweight.tasks.ScanJarForBadCalls::class) {
    badAnnotations.add("Lio/papermc/paper/annotation/DoNotUse;")
    jarToScan.set(tasks.jar.flatMap { it.archiveFile })
    classpath.from(configurations.compileClasspath)
}
tasks.check {
    dependsOn(scanJar)
}
// Paper end
// Paper start
val scanJarForOldGeneratedCode = tasks.register("scanJarForOldGeneratedCode", io.papermc.paperweight.tasks.ScanJarForOldGeneratedCode::class) {
    mcVersion.set(providers.gradleProperty("mcVersion"))
    annotation.set("Lio/papermc/paper/generated/GeneratedFrom;")
    jarToScan.set(tasks.jar.flatMap { it.archiveFile })
    classpath.from(configurations.compileClasspath)
}
tasks.check {
    dependsOn(scanJarForOldGeneratedCode)
}
// Paper end
