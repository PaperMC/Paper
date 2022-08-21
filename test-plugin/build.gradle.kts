version = "1.0.0-SNAPSHOT"

dependencies {
    compileOnly(project(":paper-api"))
    compileOnly(project(":paper-mojangapi"))
}

tasks.processResources {
    val apiVersion = rootProject.providers.gradleProperty("mcVersion").get()
        .split(".", "-").take(2).joinToString(".")
    val props = mapOf(
        "version" to project.version,
        "apiversion" to apiVersion,
    )
    inputs.properties(props)
    filesMatching("plugin.yml") {
        expand(props)
    }
}
