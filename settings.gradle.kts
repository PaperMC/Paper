import java.util.Locale

if (!file(".git").isDirectory) {
    val errorText = """
        
        =====================[ ERROR ]=====================
         The Paper project directory is not a properly cloned Git repository.
         
         In order to build Paper from source you must clone
         the Paper repository using Git, not download a code
         zip from GitHub.
         
         Built Paper jars are available for download at
         https://papermc.io/downloads
         
         See https://github.com/PaperMC/Paper/blob/master/CONTRIBUTING.md
         for further information on building and modifying Paper.
        ===================================================
    """.trimIndent()
    error(errorText)
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

rootProject.name = "paper"

for (name in listOf("Paper-API", "Paper-Server", "Paper-MojangAPI")) {
    val projName = name.toLowerCase(Locale.ENGLISH)
    include(projName)
    findProject(":$projName")!!.projectDir = file(name)
}

val testPlugin = file("test-plugin.settings.gradle.kts")
if (testPlugin.exists()) {
    apply(from = testPlugin)
} else {
    testPlugin.writeText("// Uncomment to enable the test plugin module\n//include(\":test-plugin\")\n")
}
