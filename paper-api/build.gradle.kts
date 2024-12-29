plugins {
    `java-library`
    `maven-publish`
    idea
}

java {
    withSourcesJar()
    withJavadocJar()
}

val annotationsVersion = "26.0.1"
val bungeeCordChatVersion = "1.20-R0.2"
val adventureVersion = "4.18.0"
val slf4jVersion = "2.0.9"
val log4jVersion = "2.17.1"

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
    api("com.googlecode.json-simple:json-simple:1.1.1") {
        isTransitive = false // includes junit
    }
    api("it.unimi.dsi:fastutil:8.5.15")
    api("org.apache.logging.log4j:log4j-api:$log4jVersion")
    api("org.slf4j:slf4j-api:$slf4jVersion")
    api("com.mojang:brigadier:1.3.10")

    // Deprecate bungeecord-chat in favor of adventure
    api("net.md-5:bungeecord-chat:$bungeeCordChatVersion-deprecated+build.19") {
        exclude("com.google.guava", "guava")
    }

    apiAndDocs(platform("net.kyori:adventure-bom:$adventureVersion"))
    apiAndDocs("net.kyori:adventure-api")
    apiAndDocs("net.kyori:adventure-text-minimessage")
    apiAndDocs("net.kyori:adventure-text-serializer-gson")
    apiAndDocs("net.kyori:adventure-text-serializer-legacy")
    apiAndDocs("net.kyori:adventure-text-serializer-plain")
    apiAndDocs("net.kyori:adventure-text-logger-slf4j")

    api("org.apache.maven:maven-resolver-provider:3.9.6") // make API dependency for Paper Plugins
    compileOnly("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.18")
    compileOnly("org.apache.maven.resolver:maven-resolver-transport-http:1.9.18")

    // Annotations - Slowly migrate to jspecify
    val annotations = "org.jetbrains:annotations:$annotationsVersion"
    compileOnly(annotations)
    testCompileOnly(annotations)

    val checkerQual = "org.checkerframework:checker-qual:3.33.0"
    compileOnlyApi(checkerQual)
    testCompileOnly(checkerQual)

    api("org.jspecify:jspecify:1.0.0")

    // Test dependencies
    testImplementation("org.apache.commons:commons-lang3:3.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.mockito:mockito-core:5.14.1")
    testImplementation("org.ow2.asm:asm-tree:9.7.1")
    mockitoAgent("org.mockito:mockito-core:5.14.1") { isTransitive = false } // configure mockito agent that is needed in newer java versions
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

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
        "https://javadoc.io/doc/org.joml/joml/1.10.8/index.html",
        "https://www.javadoc.io/doc/com.google.code.gson/gson/2.11.0",
        "https://jspecify.dev/docs/api/",
        "https://jd.advntr.dev/api/$adventureVersion/",
        "https://jd.advntr.dev/key/$adventureVersion/",
        "https://jd.advntr.dev/text-minimessage/$adventureVersion/",
        "https://jd.advntr.dev/text-serializer-gson/$adventureVersion/",
        "https://jd.advntr.dev/text-serializer-legacy/$adventureVersion/",
        "https://jd.advntr.dev/text-serializer-plain/$adventureVersion/",
        "https://jd.advntr.dev/text-logger-slf4j/$adventureVersion/",
        "https://javadoc.io/doc/org.slf4j/slf4j-api/$slf4jVersion/",
        "https://javadoc.io/doc/org.apache.logging.log4j/log4j-api/$log4jVersion/",
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

val scanJar = tasks.register("scanJarForBadCalls", io.papermc.paperweight.tasks.ScanJarForBadCalls::class) {
    badAnnotations.add("Lio/papermc/paper/annotation/DoNotUse;")
    jarToScan.set(tasks.jar.flatMap { it.archiveFile })
    classpath.from(configurations.compileClasspath)
}
tasks.check {
    dependsOn(scanJar)
}

val scanJarForOldGeneratedCode = tasks.register("scanJarForOldGeneratedCode", io.papermc.paperweight.tasks.ScanJarForOldGeneratedCode::class) {
    mcVersion.set(providers.gradleProperty("mcVersion"))
    annotation.set("Lio/papermc/paper/generated/GeneratedFrom;")
    jarToScan.set(tasks.jar.flatMap { it.archiveFile })
    classpath.from(configurations.compileClasspath)
}
tasks.check {
    dependsOn(scanJarForOldGeneratedCode)
}
