package io.papermc.generator.rewriter.utils;

import io.papermc.generator.Main;
import io.papermc.generator.Rewriters;
import io.papermc.generator.rewriter.registration.PaperPatternSourceSetRewriter;
import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.typewriter.SourceFile;
import io.papermc.typewriter.SourceRewriter;
import io.papermc.typewriter.context.FileMetadata;
import io.papermc.typewriter.context.IndentUnit;
import io.papermc.typewriter.parser.StringReader;
import io.papermc.typewriter.replace.CommentMarker;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import io.papermc.typewriter.replace.SearchReplaceRewriterBase;
import net.minecraft.SharedConstants;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.papermc.typewriter.replace.CommentMarker.EMPTY_MARKER;

public class ScanOldGeneratedSourceCode {

    private static final String CURRENT_VERSION;

    static {
        Main.bootStrap(false);
        CURRENT_VERSION = SharedConstants.getCurrentVersion().id();
    }

    public static void main(String[] args) throws IOException {
        PaperPatternSourceSetRewriter apiSourceSet = new PaperPatternSourceSetRewriter();
        PaperPatternSourceSetRewriter serverSourceSet = new PaperPatternSourceSetRewriter();

        Rewriters.bootstrap(apiSourceSet, serverSourceSet);

        checkOutdated(apiSourceSet, Path.of(args[0], "src/main/java"));
        checkOutdated(serverSourceSet, Path.of(args[1], "src/main/java"));
    }

    private static void checkOutdated(PaperPatternSourceSetRewriter sourceSetRewriter, Path sourceSet) throws IOException {
        IndentUnit globalIndentUnit = sourceSetRewriter.getMetadata().indentUnit();
        for (Map.Entry<SourceFile, SourceRewriter> entry : sourceSetRewriter.getRewriters().entrySet()) {
            SourceRewriter rewriter = entry.getValue();
            if (!(rewriter instanceof SearchReplaceRewriterBase srt) ||
                srt.getRewriters().stream().noneMatch(SearchReplaceRewriter::hasGeneratedComment)) {
                continue;
            }

            SourceFile file = entry.getKey();
            IndentUnit indentUnit = file.metadata().flatMap(FileMetadata::indentUnit).orElse(globalIndentUnit);
            Set<SearchReplaceRewriter> rewriters = new HashSet<>(srt.getRewriters());
            try (LineNumberReader reader = new LineNumberReader(Files.newBufferedReader(sourceSet.resolve(file.path()), StandardCharsets.UTF_8))) {
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.isEmpty()) {
                        continue;
                    }

                    CommentMarker marker = srt.searchStartMarker(new StringReader(line), indentUnit, rewriters);
                    if (marker != EMPTY_MARKER) {
                        int startIndentSize = marker.indentSize();
                        if (startIndentSize % indentUnit.size() != 0) {
                            continue;
                        }

                        String nextLine = reader.readLine();
                        if (nextLine == null) {
                            break;
                        }
                        if (nextLine.isEmpty()) {
                            continue;
                        }

                        StringReader nextLineIterator = new StringReader(nextLine);
                        int indentSize = nextLineIterator.skipChars(indentUnit.character());
                        if (indentSize != startIndentSize) {
                            continue;
                        }

                        String generatedComment = "// %s ".formatted(Annotations.annotationStyle(GeneratedFrom.class));
                        if (nextLineIterator.trySkipString(generatedComment) && nextLineIterator.canRead()) {
                            String generatedVersion = nextLineIterator.getRemaining();
                            if (!CURRENT_VERSION.equals(generatedVersion)) {
                                throw new AssertionError(
                                    "Code at line %d in %s is marked as being generated in version %s when the current version is %s".formatted(
                                    reader.getLineNumber(), file.mainClass().canonicalName(), generatedVersion, CURRENT_VERSION)
                                );
                            }

                            if (!marker.owner().getOptions().multipleOperation()) {
                                if (rewriters.remove(marker.owner()) && rewriters.isEmpty()) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
