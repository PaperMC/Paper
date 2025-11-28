import io.papermc.paperweight.checkstyle.PaperCheckstyleExt
import io.papermc.paperweight.checkstyle.PaperCheckstyleTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("io.papermc.paperweight.core") version "2.0.0-SNAPSHOT" apply false
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    val tempDisabled = setOf("paper-server", "paper-generator", "test-plugin")

    if (name !in tempDisabled) {
        apply(plugin = "io.papermc.paperweight.paper-checkstyle")
        extensions.configure<PaperCheckstyleExt> {
            val typeUseAnnotationsProvider = providers
                .fileContents(rootProject.layout.projectDirectory.file(".checkstyle/type-use-annotations.txt"))
                .asText.map { it.trim().split("\n").toSet() }
            typeUseAnnotations.set(typeUseAnnotationsProvider)
        }

        tasks.withType<PaperCheckstyleTask>().configureEach {
            configDirectory = rootProject.layout.projectDirectory.dir(".checkstyle")
            configFile = layout.projectDirectory.file(".checkstyle/checkstyle.xml").asFile
            maxHeapSize = "2g"
        }

        dependencies {
            "checkstyle"(project(":paper-checkstyle"))
        }
    }
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

subprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
        options.isFork = true
        options.compilerArgs.addAll(listOf("-Xlint:-deprecation", "-Xlint:-removal"))
    }
    tasks.withType<Javadoc>().configureEach {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources>().configureEach {
        filteringCharset = Charsets.UTF_8.name()
    }
    tasks.withType<Test>().configureEach {
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

    extensions.configure<PublishingExtension> {
        repositories {
            maven("https://artifactory.papermc.io/artifactory/snapshots/") {
                name = "paperSnapshots"
                credentials(PasswordCredentials::class)
            }
        }
    }
}

tasks.register("printMinecraftVersion") {
    val mcVersion = providers.gradleProperty("mcVersion")
    doLast {
        println(mcVersion.get().trim())
    }
}

tasks.register("printPaperVersion") {
    val paperVersion = provider { project.version }
    doLast {
        println(paperVersion.get())
    }
}
