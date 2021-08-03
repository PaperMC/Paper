version = "1.0.0-SNAPSHOT"

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly(project(":Paper-API"))
    compileOnly(project(":Paper-MojangAPI"))
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("plugin.yml") {
        expand("version" to project.version)
    }
}
