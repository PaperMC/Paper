import io.papermc.paperweight.util.*

plugins {
    java
    `maven-publish`
}

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
    implementation(project(":paper-api"))
    implementation("jline:jline:2.12.1")
    implementation("org.apache.logging.log4j:log4j-iostreams:2.24.1") // Paper - remove exclusion
    implementation("org.ow2.asm:asm-commons:9.7.1")
    implementation("commons-lang:commons-lang:2.6")
    runtimeOnly("org.xerial:sqlite-jdbc:3.47.0.0")
    runtimeOnly("com.mysql:mysql-connector-j:9.1.0")

    runtimeOnly("org.apache.maven:maven-resolver-provider:3.9.6")
    runtimeOnly("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.18")
    runtimeOnly("org.apache.maven.resolver:maven-resolver-transport-http:1.9.18")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.junit.platform:junit-platform-suite-engine:1.10.0")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.mockito:mockito-core:5.14.1")
    mockitoAgent("org.mockito:mockito-core:5.14.1") { isTransitive = false } // Paper - configure mockito agent that is needed in newer java versions
    testImplementation("org.ow2.asm:asm-tree:9.7.1")
    testImplementation("org.junit-pioneer:junit-pioneer:2.2.0") // Paper - CartesianTest
}

paperweight {
    craftBukkitPackageVersion.set("v1_21_R3") // also needs to be updated in MappingEnvironment
}

tasks.jar {
    archiveClassifier.set("dev")

    manifest {
        val git = Git(rootProject.layout.projectDirectory.path)
        val gitHash = git("rev-parse", "--short=7", "HEAD").getText().trim()
        val implementationVersion = System.getenv("BUILD_NUMBER") ?: "\"$gitHash\""
        val date = git("show", "-s", "--format=%ci", gitHash).getText().trim() // Paper
        val gitBranch = git("rev-parse", "--abbrev-ref", "HEAD").getText().trim() // Paper
        attributes(
            "Main-Class" to "org.bukkit.craftbukkit.Main",
            "Implementation-Title" to "CraftBukkit",
            "Implementation-Version" to "git-Paper-$implementationVersion",
            "Implementation-Vendor" to date, // Paper
            "Specification-Title" to "Bukkit",
            "Specification-Version" to project.version,
            "Specification-Vendor" to "Bukkit Team",
            "Git-Branch" to gitBranch, // Paper
            "Git-Commit" to gitHash, // Paper
            "CraftBukkit-Package-Version" to paperweight.craftBukkitPackageVersion.get(), // Paper
        )
        for (tld in setOf("net", "com", "org")) {
            attributes("$tld/bukkit", "Sealed" to true)
        }
    }
}

// Paper start - compile tests with -parameters for better junit parameterized test names
tasks.compileTestJava {
    options.compilerArgs.add("-parameters")
}
// Paper end

publishing {
    publications.create<MavenPublication>("maven") {
    }
}

// Paper start
val scanJar = tasks.register("scanJarForBadCalls", io.papermc.paperweight.tasks.ScanJarForBadCalls::class) {
    badAnnotations.add("Lio/papermc/paper/annotation/DoNotUse;")
    jarToScan.set(tasks.serverJar.flatMap { it.archiveFile })
    classpath.from(configurations.compileClasspath)
}
tasks.check {
    dependsOn(scanJar)
}
// Paper end

tasks.test {
    include("**/**TestSuite.class")
    workingDir = temporaryDir
    useJUnitPlatform {
        forkEvery = 1
        excludeTags("Slow")
    }
    // Paper start - configure mockito agent that is needed in newer java versions
    val provider = objects.newInstance<MockitoAgentProvider>()
    provider.fileCollection.from(mockitoAgent)
    jvmArgumentProviders.add(provider)
    // Paper end - configure mockito agent that is needed in newer java versions
}

fun TaskContainer.registerRunTask(
    name: String,
    block: JavaExec.() -> Unit
): TaskProvider<JavaExec> = register<JavaExec>(name) {
    group = "paper"
    mainClass.set("org.bukkit.craftbukkit.Main")
    standardInput = System.`in`
    workingDir = rootProject.layout.projectDirectory
        .dir(providers.gradleProperty("paper.runWorkDir").getOrElse("run"))
        .asFile
    javaLauncher.set(project.javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.JETBRAINS)
    })
    jvmArgs("-XX:+AllowEnhancedClassRedefinition", "-XX:+AllowRedefinitionToAddDeleteMethods")

    if (rootProject.childProjects["test-plugin"] != null) {
        val testPluginJar = rootProject.project(":test-plugin").tasks.jar.flatMap { it.archiveFile }
        inputs.file(testPluginJar)
        args("-add-plugin=${testPluginJar.get().asFile.absolutePath}")
    }

    args("--nogui")
    systemProperty("net.kyori.adventure.text.warnWhenLegacyFormattingDetected", true)
    if (providers.gradleProperty("paper.runDisableWatchdog").getOrElse("false") == "true") {
        systemProperty("disable.watchdog", true)
    }
    systemProperty("io.papermc.paper.suppress.sout.nags", true)

    val memoryGb = providers.gradleProperty("paper.runMemoryGb").getOrElse("2")
    minHeapSize = "${memoryGb}G"
    maxHeapSize = "${memoryGb}G"

    doFirst {
        workingDir.mkdirs()
    }

    block(this)
}

val runtimeClasspathWithoutVanillaServer = configurations.runtimeClasspath.flatMap { it.elements }
    .zip(configurations.vanillaServer.map { it.singleFile.absolutePath }) { runtime, vanilla ->
        runtime.filterNot { it.asFile.absolutePath == vanilla }
    }

tasks.registerRunTask("runServerJar") {
    description = "Spin up a test server from the serverJar archiveFile"
    classpath(tasks.serverJar.flatMap { it.archiveFile })
    classpath(runtimeClasspathWithoutVanillaServer)
}

tasks.registerRunTask("runReobf") {
    description = "Spin up a test server from the reobfJar output jar"
    classpath(tasks.reobfJar.flatMap { it.outputJar })
    classpath(runtimeClasspathWithoutVanillaServer)
}

tasks.registerRunTask("runDev") {
    description = "Spin up a non-relocated Mojang-mapped test server"
    classpath(sourceSets.main.map { it.runtimeClasspath })
    jvmArgs("-DPaper.pushPaperAssetsRoot=true")
}
