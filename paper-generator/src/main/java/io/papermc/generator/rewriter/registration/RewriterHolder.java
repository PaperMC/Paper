package io.papermc.generator.rewriter.registration;

import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.registry.RegistryIdentifiable;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.replace.ReplaceOptionsLike;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.util.function.BiFunction;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record RewriterHolder(String pattern, @Nullable ClassNamed targetClass, SearchReplaceRewriter rewriter) {

    @Contract(value = "_, _, _ -> new", pure = true)
    public static RewriterHolder holder(String pattern, @Nullable Class<?> targetClass, SearchReplaceRewriter rewriter) {
        return new RewriterHolder(pattern, targetClass == null ? null : new ClassNamed(targetClass), rewriter);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static <E, T extends SearchReplaceRewriter & RegistryIdentifiable<E>> RewriterHolder holder(String pattern, T rewriter) {
        return new RewriterHolder(pattern, RegistryEntries.byRegistryKey(rewriter.getRegistryKey()).data().api().klass().name(), rewriter);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static RewriterHolder sameHolder(String pattern, SearchReplaceRewriter rewriter) {
        return holder(pattern, null, rewriter);
    }

    public SearchReplaceRewriter transform(BiFunction<String, @Nullable ClassNamed, ReplaceOptionsLike> patternMapper) {
        return this.rewriter.withOptions(patternMapper.apply(this.pattern, this.targetClass)).customName(this.pattern);
    }
}
