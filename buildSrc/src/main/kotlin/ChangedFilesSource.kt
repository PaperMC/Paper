import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import javax.inject.Inject

abstract class ChangedFilesSource: ValueSource<Set<String>, ValueSourceParameters.None> {

    @get:Inject
    abstract val exec: ExecOperations

    private fun run(vararg args: String): String {
        val out = ByteArrayOutputStream()
        exec.exec {
            commandLine(*args)
            standardOutput = out
        }

        return String(out.toByteArray(), Charsets.UTF_8).trim()
    }

    override fun obtain(): Set<String> {
        val remoteName = run("git", "remote", "-v").split("\n").filter {
            it.contains("PaperMC/Paper", ignoreCase = true)
        }.take(1).map { it.split("\t")[0] }.singleOrNull() ?: "origin"
        run("git", "fetch", remoteName, "main", "-q")
        val mergeBase = run("git", "merge-base", "HEAD", "$remoteName/main")
        val changedFiles = run("git", "diff", "--name-only", mergeBase).split("\n").filter { it.endsWith(".java") }.toSet()
        return changedFiles
    }
}
