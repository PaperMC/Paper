import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    id("io.papermc.paperweight.core") version "1.5.9"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
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
    paramMappings("net.fabricmc:yarn:1.20.2+build.1:mergedv2")
    remapper("net.fabricmc:tiny-remapper:0.8.10:fat")
    decompiler("net.minecraftforge:forgeflower:2.0.627.2")
    spigotDecompiler("io.papermc:patched-spigot-fernflower:0.1+build.6")
    paperclip("io.papermc:paperclip:3.0.3")
}

paperweight {
    minecraftVersion.set(providers.gradleProperty("mcVersion"))
    serverProject.set(project(":paper-server"))

    paramMappingsRepo.set(paperMavenPublicUrl)
    remapRepo.set(paperMavenPublicUrl)
    decompileRepo.set(paperMavenPublicUrl)

    craftBukkit {
        fernFlowerJar.set(layout.file(spigotDecompiler.elements.map { it.single().asFile }))
    }

    paper {
        spigotApiPatchDir.set(layout.projectDirectory.dir("patches/api"))
        spigotServerPatchDir.set(layout.projectDirectory.dir("patches/server"))

        mappingsPatch.set(layout.projectDirectory.file("build-data/mappings-patch.tiny"))
        reobfMappingsPatch.set(layout.projectDirectory.file("build-data/reobf-mappings-patch.tiny"))

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
    apiCoordinates.set("io.papermc.paper:paper-api")
    mojangApiCoordinates.set("io.papermc.paper:paper-mojangapi")
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

// Uncomment while updating for a new Minecraft version
/*
tasks.collectAtsFromPatches {
    extraPatchDir.set(layout.projectDirectory.dir("patches/unapplied/server"))
}
tasks.withType<io.papermc.paperweight.tasks.RebuildGitPatches> {
    filterPatches.set(false)
}
 */

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
