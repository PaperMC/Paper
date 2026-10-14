package io.papermc.generator.rewriter.registration;

import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.registration.SourceSetRewriter;
import io.papermc.typewriter.replace.CompositeRewriter;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PatternSourceSetRewriter extends SourceSetRewriter<PatternSourceSetRewriter> {

    default PatternSourceSetRewriter register(String pattern, Class<?> targetClass, SearchReplaceRewriter rewriter) {
        return register(pattern, new ClassNamed(targetClass), rewriter);
    }

    PatternSourceSetRewriter register(String pattern, ClassNamed targetClass, SearchReplaceRewriter rewriter);

    default PatternSourceSetRewriter register(Class<?> mainClass, CompositeRewriter rewriter) {
        return this.register(new ClassNamed(mainClass), rewriter);
    }

    PatternSourceSetRewriter register(ClassNamed mainClass, CompositeRewriter rewriter);
}
