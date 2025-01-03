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

val gitUserProvider: Provider<String> = if (System.getenv("CI") == "true") {
    providers.environmentVariable("GIT_USER").map { it.trim() }
} else {
    providers.exec { commandLine("git", "config", "--get", "user.name") }.standardOutput.asText.map { it.trim() }
}
val changedFilesSource = providers.of(ChangedFilesSource::class) {}

val collectDiffedData = tasks.register<CollectDiffedDataTask>("collectDiffedData") {
    uncheckedFiles.set(providers.fileContents(localCheckstyleConfigDir.file("unchecked-files.txt")).asText.map { it.split("\n").toSet() })
    specialUsers.set(providers.fileContents(checkstyleExtraConfigDir.file("users-who-can-update.txt")).asText.map { it.split("\n").toSet() })
    changedFiles.set(changedFilesSource)
    gitUser.set(gitUserProvider)
}

val typeUseAnnotationsProvider: Provider<Set<String>> = providers.fileContents(checkstyleExtraConfigDir.file("type-use-annotations.txt"))
    .asText.map { it.trim().split("\n").toSet() }

tasks.withType<CustomCheckstyleTask> {
    rootPath = project.rootDir.path
    changedFilesTxt = collectDiffedData.flatMap { it.changedFilesTxt }
    runForAll = providers.gradleProperty("runCheckstyleForAll").map { it.toBoolean() }
    filesToRemoveFromUncheckedTxt = collectDiffedData.flatMap { it.filesToRemoveFromUncheckedTxt }
    typeUseAnnotations = typeUseAnnotationsProvider
}

