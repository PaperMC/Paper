package io.papermc.generator.rewriter.registration;

import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.registry.RegistryIdentifiable;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.registration.SourceSetRewriter;
import io.papermc.typewriter.replace.CompositeRewriter;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PatternSourceSetRewriter extends SourceSetRewriter<PatternSourceSetRewriter> {

    default <E, T extends SearchReplaceRewriter & RegistryIdentifiable<E>> PatternSourceSetRewriter register(String pattern, T rewriter) {
        return this.register(pattern, RegistryEntries.byRegistryKey(rewriter.getRegistryKey()).data().api().klass().name(), rewriter);
    }

    PatternSourceSetRewriter register(String pattern, ClassNamed targetClass, SearchReplaceRewriter rewriter);

    PatternSourceSetRewriter register(ClassNamed mainClass, CompositeRewriter rewriter);
}
