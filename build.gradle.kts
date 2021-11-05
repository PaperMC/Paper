plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.0" apply false
    id("io.papermc.paperweight.core") version "1.1.14"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(16))
        }
    }
}

subprojects {
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(16)
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }

    if (name == "Paper-MojangAPI") {
        return@subprojects
    }

    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/") {
        content { onlyForConfigurations("paperclip") }
    }
}

dependencies {
    paramMappings("org.quiltmc:yarn:1.17.1+build.1:mergedv2")
    remapper("net.fabricmc:tiny-remapper:0.6.0:fat")
    decompiler("net.minecraftforge:forgeflower:1.5.498.12")
    paperclip("io.papermc:paperclip:2.0.1")
}

paperweight {
    minecraftVersion.set(providers.gradleProperty("mcVersion"))
    serverProject.set(project(":Paper-Server"))

    paramMappingsRepo.set("https://maven.quiltmc.org/repository/release/")
    remapRepo.set("https://maven.fabricmc.net/")
    decompileRepo.set("https://files.minecraftforge.net/maven/")

    paper {
        spigotApiPatchDir.set(layout.projectDirectory.dir("patches/api"))
        spigotServerPatchDir.set(layout.projectDirectory.dir("patches/server"))

        mappingsPatch.set(layout.projectDirectory.file("build-data/mappings-patch.tiny"))
        reobfMappingsPatch.set(layout.projectDirectory.file("build-data/reobf-mappings-patch.tiny"))

        additionalSpigotMemberMappings.set(layout.projectDirectory.file("build-data/additional-spigot-member-mappings.csrg"))
        craftBukkitPatchPatchesDir.set(layout.projectDirectory.dir("build-data/craftbukkit-patch-patches"))

        reobfPackagesToFix.addAll(
            "co.aikar.timings",
            "com.destroystokyo.paper",
            "com.mojang",
            "io.papermc.paper",
            "net.kyori.adventure.bossbar",
            "net.minecraft",
            "org.bukkit.craftbukkit",
            "org.spigotmc"
        )
    }
}

tasks.generateDevelopmentBundle {
    apiCoordinates.set("io.papermc.paper:paper-api")
    mojangApiCoordinates.set("io.papermc.paper:paper-mojangapi")
    libraryRepositories.addAll(
        "https://repo.maven.apache.org/maven2/",
        "https://libraries.minecraft.net/",
        "https://papermc.io/repo/repository/maven-public/",
    )
}

publishing {
    if (project.providers.gradleProperty("publishDevBundle").forUseAtConfigurationTime().isPresent) {
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
            maven("https://papermc.io/repo/repository/maven-snapshots/") {
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
