pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}
includeBuild("../paperweight")

rootProject.name = "Paper"

include("Paper-API", "Paper-Server", "Paper-MojangAPI")
