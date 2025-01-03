import io.papermc.paperweight.tasks.BaseTask
import io.papermc.paperweight.util.cacheDir
import io.papermc.paperweight.util.deleteForcefully
import io.papermc.paperweight.util.path
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import kotlin.io.path.writeText

abstract class CollectDiffedDataTask : BaseTask() {

    @get:Input
    abstract val uncheckedFiles: SetProperty<String>

    @get:Input
    abstract val specialUsers: SetProperty<String>

    @get:Input
    abstract val changedFiles: SetProperty<String>

    @get:Input
    abstract val gitUser: Property<String>

    @get:OutputFile
    abstract val changedFilesTxt: RegularFileProperty

    @get:OutputFile
    abstract val filesToRemoveFromUncheckedTxt: RegularFileProperty

    override fun init() {
        changedFilesTxt.convention(layout.cacheDir("diffed-files").file("changed-files.txt"))
        filesToRemoveFromUncheckedTxt.convention(layout.cacheDir("diffed-files").file("files-to-remove-from-unchecked.txt"))
    }

    @TaskAction
    fun run() {
        changedFilesTxt.path.deleteForcefully()
        filesToRemoveFromUncheckedTxt.path.deleteForcefully()
        if (gitUser.get() in specialUsers.get()) {
            changedFilesTxt.path.writeText(changedFiles.get().joinToString("\n"))
            filesToRemoveFromUncheckedTxt.path.writeText(changedFiles.get().intersect(uncheckedFiles.get()).joinToString("\n"))
        } else {
            changedFilesTxt.path.writeText(changedFiles.get().minus(uncheckedFiles.get()).joinToString("\n"))
            filesToRemoveFromUncheckedTxt.path.writeText("")
        }
    }
}
