plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    id("io.papermc.paperweight.core") version "1.1.2"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(16))
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(16)
    }

    configure<PublishingExtension> {
        repositories {
            maven {
                name = "paperSnapshots"
                url = uri("https://papermc.io/repo/repository/maven-snapshots/")
                credentials(PasswordCredentials::class)
            }
        }
    }

    if (name == "Paper-MojangAPI") {
        return@subprojects
    }

    repositories {
        mavenCentral()
        maven("https://repo1.maven.org/maven2/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://ci.emc.gs/nexus/content/groups/aikar/")
        maven("https://repo.aikar.co/content/groups/aikar")
        maven("https://repo.md-5.net/content/repositories/releases/")
        maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    }
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/") {
        content {
            onlyForConfigurations("paperclip")
        }
    }
    maven("https://maven.quiltmc.org/repository/release/") {
        content {
            onlyForConfigurations("paramMappings", "remapper")
        }
    }
    maven("https://files.minecraftforge.net/maven/") {
        content {
            onlyForConfigurations("decompiler")
        }
    }
}

dependencies {
    paramMappings("org.quiltmc:yarn:1.17+build.2:mergedv2")
    remapper("org.quiltmc:tiny-remapper:0.4.1")
    decompiler("net.minecraftforge:forgeflower:1.5.498.12")
    paperclip("io.papermc:paperclip:2.0.0@jar")
}

paperweight {
    minecraftVersion.set(providers.gradleProperty("mcVersion"))
    serverProject.set(project(":Paper-Server"))

    paper {
        spigotApiPatchDir.set(file("patches/api"))
        spigotServerPatchDir.set(file("patches/server"))

        mappingsPatch.set(file("build-data/mappings-patch.tiny"))

        additionalSpigotMemberMappings.set(file("build-data/additional-spigot-member-mappings.csrg"))
        craftBukkitPatchPatchesDir.set(file("build-data/craftbukkit-patch-patches"))
    }
}

tasks.register("printMinecraftVersion") {
    doLast {
        println(providers.gradleProperty("mcVersion").forUseAtConfigurationTime().get().trim())
    }
}
