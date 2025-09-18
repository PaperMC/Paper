import io.papermc.paperweight.util.defaultJavaLauncher

plugins {
    java
    id("io.papermc.paperweight.source-generator")
}

paperweight {
    atFile = layout.projectDirectory.file("wideners.at")
}

val serverRuntimeClasspath by configurations.registering { // resolvable?
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    minecraftJar(project(":paper-server", "mappedJarOutgoing"))
    implementation(project(":paper-server", "macheMinecraftLibraries"))

    implementation("com.squareup:javapoet:1.13.0")
    implementation(project(":paper-api"))
    implementation("io.papermc.typewriter:typewriter:1.0.1") {
        isTransitive = false // paper-api already have everything
    }
    implementation("info.picocli:picocli:4.7.6")
    implementation("io.github.classgraph:classgraph:4.8.47")
    implementation("org.jetbrains:annotations:26.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    serverRuntimeClasspath(project(":paper-server", "runtimeConfiguration"))
}

val gameVersion = providers.gradleProperty("mcVersion")

val rewriteApi = tasks.registerGenerationTask("rewriteApi", true, "api", {
    bootstrapTags = true
    sourceSet = rootProject.layout.projectDirectory.dir("paper-api")
}) {
    description = "Rewrite existing API classes"
    classpath(sourceSets.main.map { it.runtimeClasspath })
}

val rewriteImpl = tasks.registerGenerationTask("rewriteImpl", true, "impl", {
    sourceSet = rootProject.layout.projectDirectory.dir("paper-server")
    serverClassPath.from(serverRuntimeClasspath)
}) {
    description = "Rewrite existing implementation classes"
    classpath(sourceSets.main.map { it.runtimeClasspath })
}

tasks.register("rewrite") {
    group = "generation"
    description = "Rewrite existing API classes and its implementation"
    dependsOn(rewriteApi, rewriteImpl)
}


val generateApi = tasks.registerGenerationTask("generateApi", false, "api", {
    bootstrapTags = true
    sourceSet = rootProject.layout.projectDirectory.dir("paper-api")
}) {
    description = "Generate new API classes"
    classpath(sourceSets.main.map { it.runtimeClasspath })
}

val generateImpl = tasks.registerGenerationTask("generateImpl", false, "impl", {
    sourceSet = rootProject.layout.projectDirectory.dir("paper-server")
}) {
    description = "Generate new implementation classes"
    classpath(sourceSets.main.map { it.runtimeClasspath })
}

tasks.register("generate") {
    group = "generation"
    description = "Generate new API classes and its implementation"
    dependsOn(generateApi, generateImpl)
}

fun TaskContainer.registerGenerationTask(
    name: String,
    rewrite: Boolean,
    side: String,
    args: (GenerationArgumentProvider.() -> Unit)? = null,
    block: JavaExec.() -> Unit
): TaskProvider<JavaExec> = register<JavaExec>(name) {
    group = "generation"
    dependsOn(project.tasks.test)
    javaLauncher = project.javaToolchains.defaultJavaLauncher(project)
    maxHeapSize = "2G"
    inputs.property("gameVersion", gameVersion)
    inputs.dir(layout.projectDirectory.dir("src/main/java")).withPathSensitivity(PathSensitivity.RELATIVE)
    mainClass = "io.papermc.generator.Main"
    systemProperty("paper.updatingMinecraft", providers.gradleProperty("updatingMinecraft").getOrElse("false").toBoolean())

    val provider = objects.newInstance<GenerationArgumentProvider>()
    provider.side = side
    provider.rewrite = rewrite
    if (args != null) {
        args(provider)
    }
    argumentProviders.add(provider)

    val targetDir = if (rewrite) "src/main/java" else "src/generated/java"
    outputs.dir(provider.sourceSet.dir(targetDir))

    block(this)
}

@Suppress("LeakingThis")
abstract class GenerationArgumentProvider : CommandLineArgumentProvider {

    @get:PathSensitive(PathSensitivity.NONE)
    @get:InputDirectory
    abstract val sourceSet: DirectoryProperty

    @get:Input
    abstract val rewrite: Property<Boolean>

    @get:Input
    abstract val side: Property<String>

    @get:CompileClasspath
    abstract val serverClassPath: ConfigurableFileCollection

    @get:Input
    @get:Optional
    abstract val bootstrapTags: Property<Boolean>

    init {
        bootstrapTags.convention(false)
    }

    override fun asArguments(): Iterable<String> {
        val args = mutableListOf<String>()

        args.add("--sourceset=${sourceSet.get().asFile.absolutePath}")
        args.add("--side=${side.get()}")
        args.add("--classpath=${serverClassPath.asPath}")

        if (rewrite.get()) {
            args.add("--rewrite")
        }

        if (bootstrapTags.get()) {
            args.add("--bootstrap-tags")
        }
        return args.toList()
    }
}

tasks.test {
    useJUnitPlatform()
}

group = "io.papermc.paper"
version = "1.0-SNAPSHOT"
