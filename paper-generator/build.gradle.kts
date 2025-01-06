import io.papermc.paperweight.util.defaultJavaLauncher

plugins {
    java
    id("io.papermc.paperweight.source-generator")
}

paperweight {
    atFile.set(layout.projectDirectory.file("wideners.at"))
}

repositories {
    mavenLocal() // todo publish typewriter somewhere
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
    implementation("io.papermc.typewriter:typewriter:1.0-SNAPSHOT") {
        isTransitive = false // paper-api already have everything
    }
    implementation("io.github.classgraph:classgraph:4.8.47")
    implementation("org.jetbrains:annotations:26.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    serverRuntimeClasspath(project(":paper-server", "runtimeConfiguration"))
}

val gameVersion = providers.gradleProperty("mcVersion")

val rewriteApi = tasks.registerGenerationTask("rewriteApi", true, "paper-api") {
    description = "Rewrite existing API classes"
    mainClass.set("io.papermc.generator.Main\$Rewriter")
    classpath(sourceSets.main.map { it.runtimeClasspath })
}

val rewriteImpl = tasks.registerGenerationTask("rewriteImpl", true, "paper-server") {
    description = "Rewrite existing implementation classes"
    mainClass.set("io.papermc.generator.Main\$Rewriter")
    classpath(sourceSets.main.map { it.runtimeClasspath })
    args(serverRuntimeClasspath.get().asPath)
}

tasks.register("rewrite") {
    group = "generation"
    description = "Rewrite existing API classes and its implementation"
    dependsOn(rewriteApi, rewriteImpl)
}


val generateApi = tasks.registerGenerationTask("generateApi", false, "paper-api") {
    description = "Generate new API classes"
    mainClass.set("io.papermc.generator.Main\$Generator")
    classpath(sourceSets.main.map { it.runtimeClasspath })
}

val generateImpl = tasks.registerGenerationTask("generateImpl", false, "paper-server") {
    description = "Generate new implementation classes"
    mainClass.set("io.papermc.generator.Main\$Generator")
    classpath(sourceSets.main.map { it.runtimeClasspath })
}

tasks.register("generate") {
    group = "generation"
    description = "Generate new API classes and its implementation"
    dependsOn(generateApi, generateImpl)
}

if (providers.gradleProperty("updatingMinecraft").getOrElse("false").toBoolean()) {
    val scanOldGeneratedSourceCode by tasks.registering(JavaExec::class) {
        group = "verification"
        description = "Scan source code to detect outdated generated code"
        javaLauncher = javaToolchains.defaultJavaLauncher(project)
        mainClass.set("io.papermc.generator.rewriter.utils.ScanOldGeneratedSourceCode")
        classpath(sourceSets.main.map { it.runtimeClasspath })

        val projectDirs = listOf("paper-api", "paper-server").map { rootProject.layout.projectDirectory.dir(it) }
        args(projectDirs.map { it.asFile.absolutePath })
        val workDirs = projectDirs.map { it.dir("src/main/java") }

        workDirs.forEach { inputs.dir(it) }
        inputs.property("gameVersion", gameVersion)
        outputs.dirs(workDirs)
    }
    tasks.check {
        dependsOn(scanOldGeneratedSourceCode)
    }
}

fun TaskContainer.registerGenerationTask(
    name: String,
    rewrite: Boolean,
    vararg targetProjects: String,
    block: JavaExec.() -> Unit
): TaskProvider<JavaExec> = register<JavaExec>(name) {
    group = "generation"
    dependsOn(project.tasks.check)
    javaLauncher = project.javaToolchains.defaultJavaLauncher(project)
    inputs.property("gameVersion", gameVersion)
    inputs.dir(layout.projectDirectory.dir("src/main/java")).withPathSensitivity(PathSensitivity.RELATIVE)
    val projectDirs = targetProjects.map { rootProject.layout.projectDirectory.dir(it) }
    args(projectDirs.map { it.asFile.absolutePath })
    systemProperty("paper.updatingMinecraft", providers.gradleProperty("updatingMinecraft").getOrElse("false").toBoolean())
    if (rewrite) {
        val source = projectDirs.map { it.dir("src/main/java") }
        source.forEach { inputs.dir(it) }
        outputs.dirs(source)
    } else {
        outputs.dirs(projectDirs.map { it.dir("src/generated/java") })
    }

    block(this)
}

tasks.test {
    useJUnitPlatform()
}

group = "io.papermc.paper"
version = "1.0-SNAPSHOT"
