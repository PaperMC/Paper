import io.papermc.paperweight.PaperweightException
import io.papermc.paperweight.tasks.BaseTask
import io.papermc.paperweight.util.*
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import java.io.ByteArrayOutputStream
import java.nio.file.Path
import java.util.regex.Pattern
import kotlin.io.path.*

plugins {
    java
    `maven-publish`
    id("io.papermc.paperweight.core") version "1.7.7"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
        options.isFork = true
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }
    tasks.withType<Test> {
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT)
        }
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
    }
}

val spigotDecompiler: Configuration by configurations.creating

repositories {
    mavenCentral()
    maven(paperMavenPublicUrl) {
        content {
            onlyForConfigurations(
                configurations.paperclip.name,
                spigotDecompiler.name,
            )
        }
    }
}

dependencies {
    paramMappings("net.fabricmc:yarn:1.21.4+build.1:mergedv2")
    remapper("net.fabricmc:tiny-remapper:0.10.3:fat")
    decompiler("org.vineflower:vineflower:1.10.1")
    spigotDecompiler("io.papermc:patched-spigot-fernflower:0.1+build.13")
    paperclip("io.papermc:paperclip:3.0.3")
}

paperweight {
    minecraftVersion = providers.gradleProperty("mcVersion")
    serverProject = project(":paper-server")

    paramMappingsRepo = paperMavenPublicUrl
    remapRepo = paperMavenPublicUrl
    decompileRepo = paperMavenPublicUrl

    craftBukkit {
        fernFlowerJar = layout.file(spigotDecompiler.elements.map { it.single().asFile })
    }

    paper {
        spigotApiPatchDir = layout.projectDirectory.dir("patches/api")
        spigotServerPatchDir = layout.projectDirectory.dir("patches/server")

        mappingsPatch = layout.projectDirectory.file("build-data/mappings-patch.tiny")
        reobfMappingsPatch = layout.projectDirectory.file("build-data/reobf-mappings-patch.tiny")

        reobfPackagesToFix.addAll(
            "co.aikar.timings",
            "com.destroystokyo.paper",
            "com.mojang",
            "io.papermc.paper",
            "ca.spottedleaf",
            "net.kyori.adventure.bossbar",
            "net.minecraft",
            "org.bukkit.craftbukkit",
            "org.spigotmc",
        )
    }
}

tasks.generateDevelopmentBundle {
    apiCoordinates = "io.papermc.paper:paper-api"
    libraryRepositories.addAll(
        "https://repo.maven.apache.org/maven2/",
        paperMavenPublicUrl,
    )
}

publishing {
    if (project.providers.gradleProperty("publishDevBundle").isPresent) {
        publications.create<MavenPublication>("devBundle") {
            artifact(tasks.generateDevelopmentBundle) {
                artifactId = "dev-bundle"
            }
        }
    }
}

allprojects {
    publishing {
        repositories {
            maven("https://repo.papermc.io/repository/maven-snapshots/") {
                name = "paperSnapshots"
                credentials(PasswordCredentials::class)
            }
        }
    }
}

tasks.register("printMinecraftVersion") {
    doLast {
        println(providers.gradleProperty("mcVersion").get().trim())
    }
}

tasks.register("printPaperVersion") {
    doLast {
        println(project.version)
    }
}

// see gradle.properties
if (providers.gradleProperty("updatingMinecraft").getOrElse("false").toBoolean()) {
    tasks.collectAtsFromPatches {
        val dir = layout.projectDirectory.dir("patches/unapplied/server")
        if (dir.path.isDirectory()) {
            extraPatchDir = dir
        }
    }
    tasks.withType<io.papermc.paperweight.tasks.RebuildGitPatches>().configureEach {
        filterPatches = false
    }
    tasks.register("continueServerUpdate", RebasePatches::class) {
        description = "Moves the next X patches from unapplied to applied, and applies them. X being the number of patches that apply cleanly, plus the terminal failure if any."
        projectDir = project.projectDir
        appliedPatches = file("patches/server")
        unappliedPatches = file("patches/unapplied/server")
        applyTaskName = "applyServerPatches"
        patchedDir = "Paper-Server"
    }
}

@UntrackedTask(because = "Does not make sense to track state")
abstract class RebasePatches : BaseTask() {
    @get:Internal
    abstract val projectDir: DirectoryProperty

    @get:InputFiles
    abstract val appliedPatches: DirectoryProperty

    @get:InputFiles
    abstract val unappliedPatches: DirectoryProperty

    @get:Input
    abstract val applyTaskName: Property<String>

    @get:Input
    abstract val patchedDir: Property<String>

    private fun unapplied(): List<Path> =
        unappliedPatches.path.listDirectoryEntries("*.patch").sortedBy { it.name }

    private fun appliedLoc(patch: Path): Path = appliedPatches.path.resolve(unappliedPatches.path.relativize(patch))

    companion object {
        val regex = Pattern.compile("Patch failed at ([0-9]{4}) (.*)")
        val continuationRegex = Pattern.compile("^\\s{1}.+\$")
        const val subjectPrefix = "Subject: [PATCH] "
    }

    @TaskAction
    fun run() {
        val patchedDirPath = projectDir.path.resolve(patchedDir.get())
        if (patchedDirPath.isDirectory()) {
            val status = Git(patchedDirPath)("status").getText()
            if (status.contains("You are in the middle of an am session.")) {
                throw PaperweightException("Cannot continue update when $patchedDirPath is in the middle of an am session.")
            }
        }

        val unapplied = unapplied()
        for (patch in unapplied) {
            patch.copyTo(appliedLoc(patch))
        }

        val out = ByteArrayOutputStream()
        val proc = ProcessBuilder()
            .directory(projectDir.path)
            .command("./gradlew", applyTaskName.get())
            .redirectErrorStream(true)
            .start()

        val f = redirect(proc.inputStream, out)

        val exit = proc.waitFor()
        f.get()

        if (exit != 0) {
            val outStr = String(out.toByteArray())
            val matcher = regex.matcher(outStr)
            if (!matcher.find()) error("Could not determine failure point")
            val failedSubjectFragment = matcher.group(2)
            val failed = unapplied.single { p ->
                p.useLines { lines ->
                    val collect = mutableListOf<String>()
                    for (line in lines) {
                        if (line.startsWith(subjectPrefix)) {
                            collect += line
                        } else if (collect.size == 1) {
                            if (continuationRegex.matcher(line).matches()) {
                                collect += line
                            } else {
                                break
                            }
                        }
                    }
                    val subjectLine = collect.joinToString("").substringAfter(subjectPrefix)
                    subjectLine.startsWith(failedSubjectFragment)
                }
            }

            // delete successful & failure point from unapplied patches dir
            for (path in unapplied) {
                path.deleteIfExists()
                if (path == failed) {
                    break
                }
            }

            // delete failed from patches dir
            var started = false
            for (path in unapplied) {
                if (path == failed) {
                    started = true
                    continue
                }
                if (started) {
                    appliedLoc(path).deleteIfExists()
                }
            }

            // Delete the build file before resetting the AM session in case it has compilation errors
            patchedDirPath.resolve("build.gradle.kts").deleteIfExists()
            // Apply again to reset the am session (so it ends on the failed patch, to allow us to rebuild after fixing it)
            val apply2 = ProcessBuilder()
                .directory(projectDir.path)
                .command("./gradlew", applyTaskName.get())
                .redirectErrorStream(true)
                .start()

            val f1 = redirect(apply2.inputStream, System.out)
            apply2.waitFor()
            f1.get()

            logger.lifecycle(outStr)
            logger.lifecycle("Patch failed at $failed; See Git output above.")
        } else {
            unapplied.forEach { it.deleteIfExists() }
            logger.lifecycle("All patches applied!")
        }

        val git = Git(projectDir.path)
        git("add", appliedPatches.path.toString() + "/*").runSilently()
        git("add", unappliedPatches.path.toString() + "/*").runSilently()
    }
}
