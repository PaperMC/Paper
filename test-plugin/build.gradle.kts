version = "1.0.0-SNAPSHOT"

dependencies {
    // compileOnly(project(":paper-api"))
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
}

tasks.processResources {
    val props = mapOf(
        "version" to project.version,
        "apiversion" to "\"${rootProject.providers.gradleProperty("apiVersion").get()}\"",
    )
    inputs.properties(props)
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}
