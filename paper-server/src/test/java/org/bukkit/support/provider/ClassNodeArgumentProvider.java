package org.bukkit.support.provider;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;
import org.bukkit.support.test.ClassNodeTest;
import org.bukkit.support.test.ClassReaderTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public class ClassNodeArgumentProvider implements ArgumentsProvider, AnnotationConsumer<ClassNodeTest> {

    private ClassNodeTest.ClassType[] classTypes;
    private Class<?>[] excludedClasses;
    private String[] excludedPackages;

    @Override
    public void accept(ClassNodeTest classNodeTest) {
        this.classTypes = classNodeTest.value();
        this.excludedClasses = classNodeTest.excludedClasses();
        this.excludedPackages = classNodeTest.excludedPackages();

        for (int i = 0; i < this.excludedPackages.length; i++) {
            this.excludedPackages[i] = this.excludedPackages[i].replace('.', '/');
        }
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        ClassReaderArgumentProvider classReaderArgumentProvider = new ClassReaderArgumentProvider();
        classReaderArgumentProvider.accept(new ClassReaderArguments(this.classReaderClassType(), this.excludedClasses, this.excludedPackages));

        return classReaderArgumentProvider.getClassReaders().map(this::toClassNode).map(cn -> Arguments.of(cn, cn.name));
    }

    private ClassReaderTest.ClassType[] classReaderClassType() {
        ClassReaderTest.ClassType[] newValues = new ClassReaderTest.ClassType[this.classTypes.length];

        for (int i = 0; i < this.classTypes.length; i++) {
            newValues[i] = switch (this.classTypes[i]) {
                case BUKKIT -> ClassReaderTest.ClassType.BUKKIT;
                case CRAFT_BUKKIT -> ClassReaderTest.ClassType.CRAFT_BUKKIT;
            };
        }

        return newValues;
    }

    private ClassNode toClassNode(ClassReader classReader) {
        ClassNode classNode = new ClassNode(Opcodes.ASM9);

        classReader.accept(classNode, Opcodes.ASM9);

        return classNode;
    }

    private record ClassReaderArguments(ClassType[] value, Class<?>[] excludedClasses, String[] excludedPackages) implements ClassReaderTest {

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    }
}
