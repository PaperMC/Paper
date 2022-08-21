import java.util.Locale

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
