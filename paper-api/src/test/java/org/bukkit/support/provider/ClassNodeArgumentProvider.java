package org.bukkit.support.provider;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;
import org.bukkit.support.test.ClassNodeTest;
import org.bukkit.support.test.ClassReaderTest;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.jupiter.params.support.ParameterDeclarations;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

@NullMarked
public class ClassNodeArgumentProvider implements ArgumentsProvider, AnnotationConsumer<ClassNodeTest> {

    private Class<?>[] excludedClasses = new Class<?>[0];
    private String[] excludedPackages = new String[0];

    @Override
    public void accept(final ClassNodeTest classNodeTest) {
        this.excludedClasses = classNodeTest.excludedClasses();
        this.excludedPackages = classNodeTest.excludedPackages();

        for (int i = 0; i < this.excludedPackages.length; i++) {
            this.excludedPackages[i] = this.excludedPackages[i].replace('.', '/');
        }
    }

    @Override
    public Stream<? extends Arguments> provideArguments(final ParameterDeclarations declarations, final ExtensionContext extensionContext) {
        final ClassReaderArgumentProvider classReaderArgumentProvider = new ClassReaderArgumentProvider();
        classReaderArgumentProvider.accept(new ClassReaderArguments(this.excludedClasses, this.excludedPackages));

        return classReaderArgumentProvider.getClassReaders().map(this::toClassNode).map(Arguments::of);
    }

    private ClassNode toClassNode(final ClassReader classReader) {
        final ClassNode classNode = new ClassNode(Opcodes.ASM9);

        classReader.accept(classNode, Opcodes.ASM9);

        return classNode;
    }

    private record ClassReaderArguments(Class<?>[] excludedClasses, String[] excludedPackages) implements ClassReaderTest {

        @Override
        public Class<? extends Annotation> annotationType() {
            return ClassReaderTest.class;
        }
    }
}
