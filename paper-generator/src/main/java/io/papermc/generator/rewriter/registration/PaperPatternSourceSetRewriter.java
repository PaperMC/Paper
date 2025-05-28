package io.papermc.generator.rewriter.registration;

import io.papermc.generator.rewriter.utils.Annotations;
import io.papermc.generator.types.SimpleGenerator;
import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.SourceFile;
import io.papermc.typewriter.context.IndentUnit;
import io.papermc.typewriter.context.SourcesMetadata;
import io.papermc.typewriter.registration.SourceSetRewriterImpl;
import io.papermc.typewriter.replace.CompositeRewriter;
import io.papermc.typewriter.replace.ReplaceOptions;
import io.papermc.typewriter.replace.ReplaceOptionsLike;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.SharedConstants;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.VisibleForTesting;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class PaperPatternSourceSetRewriter extends SourceSetRewriterImpl<PatternSourceSetRewriter> implements PatternSourceSetRewriter {

    private static final String COMMENT_MARKER_FORMAT = "%s generate - %s"; // {0} = Start|End {1} = pattern
    private static final IndentUnit INDENT_UNIT = IndentUnit.parse(SimpleGenerator.INDENT_UNIT);

    public PaperPatternSourceSetRewriter() {
        this(Collections.emptySet());
    }

    public PaperPatternSourceSetRewriter(Set<Path> classpath) {
        super(SourcesMetadata.of(INDENT_UNIT, b -> b.classpath(classpath))); // let the runtime java version since it will always be in sync with what paperweight use
    }

    @VisibleForTesting
    public SourcesMetadata getMetadata() {
        return this.metadata;
    }

    private static ReplaceOptionsLike getOptions(String pattern, @Nullable ClassNamed targetClass) {
        return ReplaceOptions.between(
                COMMENT_MARKER_FORMAT.formatted("Start", pattern),
                COMMENT_MARKER_FORMAT.formatted("End", pattern)
            )
            .generatedComment(Annotations.annotationStyle(GeneratedFrom.class) + " " + SharedConstants.getCurrentVersion().id())
            .targetClass(targetClass);
    }

    @Override
    public PatternSourceSetRewriter register(String pattern, ClassNamed targetClass, SearchReplaceRewriter rewriter) {
        return super.register(SourceFile.of(targetClass.topLevel()), rewriter.withOptions(getOptions(pattern, targetClass)).customName(pattern));
    }

    @Override
    public PatternSourceSetRewriter register(ClassNamed mainClass, CompositeRewriter rewriter) {
        return super.register(SourceFile.of(mainClass), rewriter);
    }

    @Contract(value = "_ -> new", pure = true)
    public static CompositeRewriter composite(RewriterHolder... holders) {
        return CompositeRewriter.bind(Arrays.stream(holders)
            .map(holder -> holder.transform(PaperPatternSourceSetRewriter::getOptions))
            .toArray(SearchReplaceRewriter[]::new));
    }
}
