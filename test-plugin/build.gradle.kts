version = "1.0.0-SNAPSHOT"
repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // compileOnly(project(":paper-api"))
    // compileOnly(project(":paper-mojangapi"))
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

tasks.processResources {
    val apiVersion = rootProject.providers.gradleProperty("mcVersion").get()
        .split(".", "-").take(2).joinToString(".")
    val props = mapOf(
        "version" to project.version,
        "apiversion" to "\"$apiVersion\"",
    )
    inputs.properties(props)
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}
