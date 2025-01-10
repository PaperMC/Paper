import io.papermc.paperweight.util.convention
import java.nio.file.Path

abstract class CustomCheckstylePlugin : CheckstylePlugin() {

    override fun getTaskType(): Class<Checkstyle> {
        @Suppress("UNCHECKED_CAST")
        return CustomCheckstyleTask::class.java as Class<Checkstyle>
    }
}

apply {
   plugin(CustomCheckstylePlugin::class.java)
}

// config dir for checkstyle extras
val checkstyleExtraConfigDir = objects.directoryProperty().convention(rootProject, Path.of(".checkstyle"))
val localCheckstyleConfigDir = objects.directoryProperty().convention(project, Path.of(".checkstyle"))

extensions.configure<CheckstyleExtension>() {
    toolVersion = "10.21.0"
    configDirectory = localCheckstyleConfigDir
}

val typeUseAnnotationsProvider: Provider<Set<String>> = providers.fileContents(checkstyleExtraConfigDir.file("type-use-annotations.txt"))
    .asText.map { it.trim().split("\n").toSet() }

val packagesToSkipSource: Provider<Set<String>> = providers.fileContents(localCheckstyleConfigDir.file("packages.txt")).asText.map { it.trim().split("\n").toSet() }

tasks.withType<CustomCheckstyleTask> {
    rootPath = project.rootDir.path
    packagesToSkip = packagesToSkipSource
    typeUseAnnotations = typeUseAnnotationsProvider
}

