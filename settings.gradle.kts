import java.util.Locale

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
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
         
         See https://github.com/PaperMC/Paper/blob/master/CONTRIBUTING.md
         for further information on building and modifying Paper.
        ===================================================
    """.trimIndent()
    error(errorText)
}

rootProject.name = "paper"

for (name in listOf("Paper-API", "Paper-Server", "Paper-MojangAPI")) {
    val projName = name.lowercase(Locale.ENGLISH)
    include(projName)
    findProject(":$projName")!!.projectDir = file(name)
}

mapOf("test-plugin.settings.gradle.kts" to """
        // Uncomment to enable the test plugin module
        // include(":test-plugin")
    """.trimIndent(),
    "paper-api-generator.settings.gradle.kts" to """
        // Uncomment to enable the api generator module
        // include(":paper-api-generator")
    """.trimIndent()
).forEach { (fileName, text) ->
    val settingsFile = file(fileName)
    if (settingsFile.exists()) {
        apply(from = settingsFile)
    } else {
        settingsFile.writeText(text + "\n")
    }
}
