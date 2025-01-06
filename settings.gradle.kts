import java.util.Locale

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

if (!file(".git").exists()) {
    val errorText = """
        
        =====================[ ERROR ]=====================
         The Paper project directory is not a properly cloned Git repository.
         
         In order to build Paper from source you must clone
         the Paper repository using Git, not download a code
         zip from GitHub.
         
         Built Paper jars are available for download at
         https://papermc.io/downloads/paper
         
         See https://github.com/PaperMC/Paper/blob/main/CONTRIBUTING.md
         for further information on building and modifying Paper.
        ===================================================
    """.trimIndent()
    error(errorText)
}

rootProject.name = "paper"

for (name in listOf("paper-api", "paper-server")) {
    include(name)
    file(name).mkdirs()
}

optionalInclude("test-plugin")

if (providers.gradleProperty("updatingMinecraft").getOrElse("false").toBoolean()) {
    include("paper-generator")
} else {
    optionalInclude("paper-generator")
}

fun optionalInclude(name: String, op: (ProjectDescriptor.() -> Unit)? = null) {
    val settingsFile = file("$name.settings.gradle.kts")
    if (settingsFile.exists()) {
        apply(from = settingsFile)
        findProject(":$name")?.let { op?.invoke(it) }
    } else {
        settingsFile.writeText(
            """
            // Uncomment to enable the '$name' project
            // include(":$name")

            """.trimIndent()
        )
    }
}
