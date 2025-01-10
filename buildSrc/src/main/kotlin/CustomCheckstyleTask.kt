import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.nio.file.Paths
import kotlin.io.path.relativeTo

abstract class CustomCheckstyleTask : Checkstyle() {

    @get:Input
    abstract val rootPath: Property<String>

    @get:Input
    abstract val packagesToSkip: SetProperty<String>

    @get:Input
    abstract val typeUseAnnotations: SetProperty<String>

    @TaskAction
    override fun run() {
        val existingProperties = configProperties?.toMutableMap() ?: mutableMapOf()
        existingProperties["type_use_annotations"] = typeUseAnnotations.get().joinToString("|")
        configProperties = existingProperties
        exclude {
            if (it.isDirectory) return@exclude false
            val absPath = it.file.toPath().toAbsolutePath().relativeTo(Paths.get(rootPath.get()))
            val parentPath = (absPath.parent?.toString() + "/")
            packagesToSkip.get().any { pkg -> parentPath == pkg }
        }
        if (!source.isEmpty) {
            super.run()
        }
    }
}
