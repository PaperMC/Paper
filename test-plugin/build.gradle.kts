version = "1.0.0-SNAPSHOT"

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly(project(":paper-api"))
    compileOnly(project(":paper-mojangapi"))
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}
