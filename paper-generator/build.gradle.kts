import io.papermc.paperweight.util.defaultJavaLauncher

plugins {
    java
    id("io.papermc.paperweight.source-generator")
}

paperweight {
    atFile.set(layout.projectDirectory.file("wideners.at"))
}

dependencies {
    minecraftJar(project(":paper-server", "mappedJarOutgoing"))
    implementation(project(":paper-server", "macheMinecraftLibraries"))

    implementation("com.squareup:javapoet:1.13.0")
    implementation("io.papermc.typewriter:typewriter:1.0.2-20250601.125803-3")
    implementation("info.picocli:picocli:4.7.6")
    implementation("io.github.classgraph:classgraph:4.8.179")
    implementation("org.jetbrains:annotations:26.0.2")
    implementation("org.jspecify:jspecify:1.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

val gameVersion = providers.gradleProperty("mcVersion")

val rewriteApi = tasks.registerGenerationTask("rewriteApi", true, "api", {
    bootstrapTags = true
    sourceSet = "paper-api/src/main/java"
}) {
    description = "Rewrite existing API classes"
    classpath(sourceSets.main.map { it.runtimeClasspath })
}

val rewriteImpl = tasks.registerGenerationTask("rewriteImpl", true, "impl", {
    bootstrapTags = true // needed for CraftItemMetasRewriter, remove once item meta is gone
    sourceSet = "paper-server/src/main/java"
}) {
    description = "Rewrite existing implementation classes"
    classpath(sourceSets.main.map { it.runtimeClasspath })
}

val rewriteImplTest = tasks.registerGenerationTask("rewriteImplTest", true, "impl-test", {
    sourceSet = "paper-server/src/test/java"
}) {
    description = "Rewrite existing implementation test classes"
    classpath(sourceSets.main.map { it.runtimeClasspath })
}

tasks.register("rewrite") {
    group = "generation"
    description = "Rewrite existing API classes and its implementation"
    dependsOn(rewriteApi, rewriteImpl, rewriteImplTest)
}


val generateApi = tasks.registerGenerationTask("generateApi", false, "api", {
    bootstrapTags = true
    sourceSet = "paper-api/src/generated/java"
}) {
    description = "Generate new API classes"
    classpath(sourceSets.main.map { it.runtimeClasspath })
}

val generateImpl = tasks.registerGenerationTask("generateImpl", false, "impl", {
    sourceSet = "paper-server/src/generated/java"
}) {
    description = "Generate new implementation classes"
    classpath(sourceSets.main.map { it.runtimeClasspath })
}

tasks.register("generate") {
    group = "generation"
    description = "Generate new API classes and its implementation"
    dependsOn(generateApi, generateImpl)
}

tasks.register<JavaExec>("prepareInputFiles") {
    group = "generation"
    description = "Prepare input files by sorting them (and updating them if possible)"
    javaLauncher = javaToolchains.defaultJavaLauncher(project)
    mainClass.set("io.papermc.generator.tasks.PrepareInputFiles")
    classpath(sourceSets.main.map { it.runtimeClasspath })

    inputs.property("gameVersion", gameVersion)
    val resourceDir = layout.projectDirectory.dir("src/main/resources")
    args(resourceDir.asFile.absolutePath)
    inputs.dir(resourceDir)
    outputs.dir(resourceDir)
}

if (providers.gradleProperty("updatingMinecraft").getOrElse("false").toBoolean()) {
    val scanOldGeneratedSourceCode by tasks.registering(JavaExec::class) {
        group = "verification"
        description = "Scan source code to detect outdated generated code"
        javaLauncher = javaToolchains.defaultJavaLauncher(project)
        mainClass.set("io.papermc.generator.tasks.ScanOldGeneratedSourceCode")
        classpath(sourceSets.main.map { it.runtimeClasspath })

        val projectDirs = listOf("paper-api", "paper-server").map { rootProject.layout.projectDirectory.dir(it) }
        args(projectDirs.map { it.asFile.absolutePath })
        val workDirs = projectDirs.map { it.dir("src/main/java") }.plus(rootProject.layout.projectDirectory.dir("paper-server/src/test/java"))

        workDirs.forEach { inputs.dir(it) }
        inputs.property("gameVersion", gameVersion)
    }
    tasks.check {
        dependsOn(scanOldGeneratedSourceCode)
    }
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
    inputs.property("gameVersion", gameVersion)
    inputs.dir(layout.projectDirectory.dir("src/main/")).withPathSensitivity(PathSensitivity.RELATIVE)
    mainClass.set("io.papermc.generator.Main")
    systemProperty("paper.updatingMinecraft", providers.gradleProperty("updatingMinecraft").getOrElse("false").toBoolean())

    val provider = objects.newInstance<GenerationArgumentProvider>()
    provider.side = side
    provider.rewrite = rewrite
    provider.rootDir = rootProject.layout.projectDirectory
    if (args != null) {
        args(provider)
    }
    argumentProviders.add(provider)
    // outputs.dir(provider.sourceSet)

    block(this)
}

@Suppress("LeakingThis")
abstract class GenerationArgumentProvider : CommandLineArgumentProvider {

    @get:PathSensitive(PathSensitivity.NONE)
    @get:InputDirectory
    abstract val rootDir: DirectoryProperty

    @get:Input
    abstract val sourceSet: Property<String>

    @get:Input
    abstract val rewrite: Property<Boolean>

    @get:Input
    abstract val side: Property<String>

    @get:Input
    @get:Optional
    abstract val bootstrapTags: Property<Boolean>

    init {
        bootstrapTags.convention(false)
    }

    override fun asArguments(): Iterable<String> {
        val args = mutableListOf<String>()

        args.add("--root-dir=${rootDir.get().asFile.absolutePath}")
        args.add("--sourceset=${rootDir.get().dir(sourceSet.get()).asFile.absolutePath}")
        args.add("--side=${side.get()}")

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
tasks.compileTestJava {
    options.compilerArgs.add("-parameters")
}

group = "io.papermc.paper"
version = "1.0-SNAPSHOT"
