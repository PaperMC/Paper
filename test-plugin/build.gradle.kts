version = "1.0.0-SNAPSHOT"

dependencies {
    compileOnly(project(":paper-api"))
}

tasks.processResources {
    var apiVersion = rootProject.providers.gradleProperty("mcVersion").get()
    // Bukkit api versioning does not support suffixed versions
    apiVersion = apiVersion.substringBefore('-')
    apiVersion = "1.21.11" // TODO - snapshot - remove once pre-releases hit

    val props = mapOf(
        "version" to project.version,
        "apiversion" to "\"$apiVersion\"",
    )
    inputs.properties(props)
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}
