import io.papermc.paperweight.util.path
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.nio.file.Paths
import kotlin.io.path.readLines
import kotlin.io.path.relativeTo

abstract class CustomCheckstyleTask : Checkstyle() {

    @get:Input
    abstract val rootPath: Property<String>

    @get:InputFile
    abstract val changedFilesTxt: RegularFileProperty

    @get:Input
    @get:Optional
    abstract val runForAll: Property<Boolean>

    @get:InputFile
    abstract val filesToRemoveFromUncheckedTxt: RegularFileProperty

    @get:Input
    abstract val typeUseAnnotations: SetProperty<String>

    @TaskAction
    override fun run() {
        val diffedFiles = changedFilesTxt.path.readLines().filterNot { it.isBlank() }.toSet()
        val existingProperties = configProperties?.toMutableMap() ?: mutableMapOf()
        existingProperties["type_use_annotations"] = typeUseAnnotations.get().joinToString("|")
        configProperties = existingProperties
        include { fileTreeElement ->
            if (fileTreeElement.isDirectory || runForAll.getOrElse(false)) {
                return@include true
            }
            val absPath = fileTreeElement.file.toPath().toAbsolutePath().relativeTo(Paths.get(rootPath.get()))
            return@include diffedFiles.contains(absPath.toString())
        }
        if (!source.isEmpty) {
            super.run()
        }
        val uncheckedFiles = filesToRemoveFromUncheckedTxt.path.readLines().filterNot { it.isBlank() }.toSet()
        if (uncheckedFiles.isNotEmpty()) {
            error("Remove the following files from unchecked-files.txt: ${uncheckedFiles.joinToString("\n\t", prefix = "\n")}")
        }
    }
}
