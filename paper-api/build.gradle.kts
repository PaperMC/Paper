plugins {
    `java-library`
    `maven-publish`
    idea
}

java {
    withSourcesJar()
    withJavadocJar()
}

val annotationsVersion = "26.0.2"
// Keep in sync with paper-server adventure-text-serializer-ansi dep
val adventureVersion = "4.21.0-mc1215-SNAPSHOT" // FIXME move to release asap
val adventureJavadocVersion = "4.20.0" // Fixme remove me
val bungeeCordChatVersion = "1.21-R0.2-deprecated+build.21"
val slf4jVersion = "2.0.16"
val log4jVersion = "2.24.1"

val apiAndDocs: Configuration by configurations.creating {
    attributes {
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.DOCUMENTATION))
        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named(DocsType.SOURCES))
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    }
}
configurations.api {
    extendsFrom(apiAndDocs)
}

// Configure mockito agent that is needed in newer Java versions
val mockitoAgent = configurations.register("mockitoAgent")
abstract class MockitoAgentProvider : CommandLineArgumentProvider {
    @get:CompileClasspath
    abstract val fileCollection: ConfigurableFileCollection

    override fun asArguments(): Iterable<String> {
        return listOf("-javaagent:" + fileCollection.files.single().absolutePath)
    }
}

dependencies {
    // api dependencies are listed transitively to API consumers
    api("com.google.guava:guava:33.3.1-jre")
    api("com.google.code.gson:gson:2.11.0")
    api("org.yaml:snakeyaml:2.2")
    api("org.joml:joml:1.10.8") {
        isTransitive = false // https://github.com/JOML-CI/JOML/issues/352
    }
    api("it.unimi.dsi:fastutil:8.5.15")
    api("org.apache.logging.log4j:log4j-api:$log4jVersion")
    api("org.slf4j:slf4j-api:$slf4jVersion")
    api("com.mojang:brigadier:1.3.10")

    // Deprecate bungeecord-chat in favor of adventure
    api("net.md-5:bungeecord-chat:$bungeeCordChatVersion") {
        exclude("com.google.guava", "guava")
    }

    // FIXME remove me when we are using a release again
    val adventureGroup = "io.papermc.adventure"
    apiAndDocs(platform("$adventureGroup:adventure-bom:$adventureVersion"))
    apiAndDocs("$adventureGroup:adventure-api")
    apiAndDocs("$adventureGroup:adventure-text-minimessage")
    apiAndDocs("$adventureGroup:adventure-text-serializer-gson")
    apiAndDocs("$adventureGroup:adventure-text-serializer-legacy")
    apiAndDocs("$adventureGroup:adventure-text-serializer-plain")
    apiAndDocs("$adventureGroup:adventure-text-logger-slf4j")

    api("org.apache.maven:maven-resolver-provider:3.9.6") // make API dependency for Paper Plugins
    compileOnly("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.18")
    compileOnly("org.apache.maven.resolver:maven-resolver-transport-http:1.9.18")

    // Annotations - Slowly migrate to jspecify
    val annotations = "org.jetbrains:annotations:$annotationsVersion"
    compileOnly(annotations)
    testCompileOnly(annotations)

    val checkerQual = "org.checkerframework:checker-qual:3.49.2"
    compileOnlyApi(checkerQual)
    testCompileOnly(checkerQual)

    api("org.jspecify:jspecify:1.0.0")

    // Test dependencies
    testImplementation("org.apache.commons:commons-lang3:3.17.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.mockito:mockito-core:5.14.1")
    testImplementation("org.ow2.asm:asm-tree:9.8")
    mockitoAgent("org.mockito:mockito-core:5.14.1") { isTransitive = false } // configure mockito agent that is needed in newer java versions
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

val generatedDir: java.nio.file.Path = layout.projectDirectory.dir("src/generated/java").asFile.toPath()
idea {
    module {
        generatedSourceDirs.add(generatedDir.toFile())
    }
}
sourceSets {
    main {
        java {
            srcDir(generatedDir)
        }
    }
}

val outgoingVariants = arrayOf("runtimeElements", "apiElements", "sourcesElements", "javadocElements")
val mainCapability = "${project.group}:${project.name}:${project.version}"
configurations {
    val outgoing = outgoingVariants.map { named(it) }
    for (config in outgoing) {
        config {
            attributes {
                attribute(io.papermc.paperweight.util.mainCapabilityAttribute, mainCapability)
            }
            outgoing {
                capability(mainCapability)
                // Paper-MojangAPI has been merged into Paper-API
                capability("io.papermc.paper:paper-mojangapi:${project.version}")
                capability("com.destroystokyo.paper:paper-mojangapi:${project.version}")
                // Conflict with old coordinates
                capability("com.destroystokyo.paper:paper-api:${project.version}")
                capability("org.spigotmc:spigot-api:${project.version}")
                capability("org.bukkit:bukkit:${project.version}")
            }
        }
    }
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("maven") {
        // For Brigadier API
        outgoingVariants.forEach {
            suppressPomMetadataWarningsFor(it)
        }
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

abstract class Services {
    @get:Inject
    abstract val fileSystemOperations: FileSystemOperations
}
val services = objects.newInstance<Services>()

tasks.withType<Javadoc> {
    val options = options as StandardJavadocDocletOptions
    options.overview = "src/main/javadoc/overview.html"
    options.use()
    options.isDocFilesSubDirs = true
    options.links(
        "https://guava.dev/releases/33.3.1-jre/api/docs/",
        "https://javadoc.io/doc/org.yaml/snakeyaml/2.2/",
        "https://javadoc.io/doc/org.jetbrains/annotations/$annotationsVersion/",
        "https://javadoc.io/doc/org.joml/joml/1.10.8/",
        "https://www.javadoc.io/doc/com.google.code.gson/gson/2.11.0",
        "https://jspecify.dev/docs/api/",
        "https://jd.advntr.dev/api/$adventureJavadocVersion/",
        "https://jd.advntr.dev/key/$adventureJavadocVersion/",
        "https://jd.advntr.dev/text-minimessage/$adventureJavadocVersion/",
        "https://jd.advntr.dev/text-serializer-gson/$adventureJavadocVersion/",
        "https://jd.advntr.dev/text-serializer-legacy/$adventureJavadocVersion/",
        "https://jd.advntr.dev/text-serializer-plain/$adventureJavadocVersion/",
        "https://jd.advntr.dev/text-logger-slf4j/$adventureJavadocVersion/",
        "https://javadoc.io/doc/org.slf4j/slf4j-api/$slf4jVersion/",
        "https://logging.apache.org/log4j/2.x/javadoc/log4j-api/",
        "https://javadoc.io/doc/org.apache.maven.resolver/maven-resolver-api/1.7.3",
    )
    options.tags("apiNote:a:API Note:")

    inputs.files(apiAndDocs).ignoreEmptyDirectories().withPropertyName(apiAndDocs.name + "-configuration")
    val apiAndDocsElements = apiAndDocs.elements
    doFirst {
        options.addStringOption(
            "sourcepath",
            apiAndDocsElements.get().map { it.asFile }.joinToString(separator = File.pathSeparator, transform = File::getPath)
        )
    }

    // workaround for https://github.com/gradle/gradle/issues/4046
    inputs.dir("src/main/javadoc").withPropertyName("javadoc-sourceset")
    val fsOps = services.fileSystemOperations
    doLast {
        fsOps.copy {
            from("src/main/javadoc") {
                include("**/doc-files/**")
            }
            into("build/docs/javadoc")
        }
    }
}

tasks.test {
    useJUnitPlatform()

    // configure mockito agent that is needed in newer java versions
    val provider = objects.newInstance<MockitoAgentProvider>()
    provider.fileCollection.from(mockitoAgent)
    jvmArgumentProviders.add(provider)
}

// Compile tests with -parameters for better junit parameterized test names
tasks.compileTestJava {
    options.compilerArgs.add("-parameters")
}

val scanJarForBadCalls by tasks.registering(io.papermc.paperweight.tasks.ScanJarForBadCalls::class) {
    badAnnotations.add("Lio/papermc/paper/annotation/DoNotUse;")
    jarToScan.set(tasks.jar.flatMap { it.archiveFile })
    classpath.from(configurations.compileClasspath)
}
tasks.check {
    dependsOn(scanJarForBadCalls)
}

if (providers.gradleProperty("updatingMinecraft").getOrElse("false").toBoolean()) {
    val scanJarForOldGeneratedCode by tasks.registering(io.papermc.paperweight.tasks.ScanJarForOldGeneratedCode::class) {
        mcVersion.set(providers.gradleProperty("mcVersion"))
        annotation.set("Lio/papermc/paper/generated/GeneratedFrom;")
        jarToScan.set(tasks.jar.flatMap { it.archiveFile })
        classpath.from(configurations.compileClasspath)
    }
    tasks.check {
        dependsOn(scanJarForOldGeneratedCode)
    }
}
